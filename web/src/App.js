import { GoogleLogin } from 'react-google-login'
import './App.css'
import { useState, useEffect } from "react";
import { gapi } from 'gapi-script';


const { google } = require('googleapis');
const CLIENT_ID = '26342651488-daqciiut6qikektlcu6oj6h21iij33cr.apps.googleusercontent.com';
const CLIENT_SECRET = '26342651488-daqciiut6qikektlcu6oj6h21iij33cr.apps.googleusercontent.com';
const REDIRECT_URI = 'https://developers.google.com/oauthplayground';
const REFRESH_TOKEN = '1//04rAv1_DeGexRCgYIARAAGAQSNwF-L9IrEHpUOzYCI2MTDki5_FBo-wqwFmrIhvnV3_iasesSX7fPkQL8insIERQJEbGvLfWLloY';

const oauth2Client = new google.auth.OAuth2(CLIENT_ID, CLIENT_SECRET, REDIRECT_URI);
oauth2Client.setCredentials({refresh_token: REFRESH_TOKEN});

function App() { // defining app component 
  const backendUrl = "https://thebuzzaws.herokuapp.com/";

  //defining states (memory of components) 
  // useState gives [variable that can change,  function that changes variable] re-renders App component
  const [screen, setScreen] = useState("login");  // screen keeps track of screen user is on
  const [whosLoggedIn, setWhosLoggedIn] = useState(null);  // keeping track of who's logged in 
  const [messages, setMessages] = useState([]); // keeps track of messages in table

  const [newSubject, setNewSubject] = useState(null);
  const [newMessage, setNewMessage] = useState(null);
  const [newComment, setNewComment] = useState(null);

  const [selectedMessage, setSelectedMessage] = useState(null); // has content of currently selected message and id

  const [currentMessageComments, setCurrentMessageComments] = useState([]); //hold comments of current message

  const [loginData, setLoginData] = useState(
    localStorage.getItem('loginData')
      ? JSON.parse(localStorage.getItem('loginData'))
      : null
  );

  // get all messages on applications start
  useEffect(() => {
    getMessages();
  }, []);

  useEffect(() => {
    function start() {
      gapi.client.init({
        client_id: "26342651488-daqciiut6qikektlcu6oj6h21iij33cr.apps.googleusercontent.com",
        scope: ""
      })
    };
    gapi.load('client:auth2', start);
  });

  //defining arrow function responseGoogle re-renders App component based on response
  const responseGoogle = async (response) => {
    console.log(response); // Let's parse what we want instead
    if (response && response.profileObj) {
      setWhosLoggedIn(response.profileObj.email);
      setScreen("messages");
      console.log("Hello from google reponse!"); // just a checkpoint
      var user_profile = response.getBasicProfile();
      var id_token = response.getAuthResponse().id_token;
      var user_email = user_profile.getEmail();
      var fullname = user_profile.getName();
      sessionStorage.setItem("name", user_profile.getName());
      sessionStorage.setItem("email", user_profile.getEmail());
      console.log('Full Name: ' + user_profile.getName());
      console.log("Email: " + user_profile.getEmail());
      console.log("ID Token: " + id_token);
      console.log("need to make new put route to send this bitch");

      // need to implement new route via fetch
      const data = {
        name: fullname, email: user_email,
        id_token: id_token
      };
      fetch(backendUrl + 'oauth', {
        method: 'POST',
        body: JSON.stringify(data)
      }).then(() => {
        console.log('TOKEN HAS BEEN SENT TO BACKEND!');
      });
    }
  }




  const getMessages = () => {
    fetch(backendUrl + "messages").then(res => {
      if (!res.ok) {
        throw Error('could not fetch data');
      }
      return res.json();
    }).then(messages => {
      console.log(messages.mData);
      setMessages(messages.mData)
    }).catch(err => {
      if (err.name === 'AbortError') {
        console.log('fetch aborted');
      } else {
        console.log('print other certain error');
      }
    });
  }

  const postMessage = () => {
    if (!newSubject || !newMessage) {
      alert("Make sure there's a subject & message.");
      return;
    }

    fetch(backendUrl + "messages", {
      method: "POST",
      body: JSON.stringify(
        {
          mTitle: newSubject,
          mMessage: newMessage,
          mLikeCount: 0,
          mDislikeCount: 0
        }
      ),
      headers: {
        "Content-type": "application/json"
      }
    })
      .then(response => response.text())
      .then(data => console.log(data))
      .then(() => clearMessageForm)
      .then(() => getMessages())
  }

  const postComment = () => {
    if (!newComment) {
      alert("Make sure there's a comment.");
      return;
    }
  }

  const getMessageComments = () => {

  }

  const onSubjectChange = (subject) => {
    setNewSubject(subject);
  }

  const onCommentChange = (comment) => {
    setNewComment(comment);
  }

  const clearCommentForm = () => {
    setNewMessage(null);
  }

  const onMessageChange = (message) => {
    setNewMessage(message);
  }

  const clearMessageForm = () => {
    setNewSubject(null);
    setNewMessage(null);
  }

  const onLike = (messageId) => {
    // This will increment the likes of the given place utilizing the put
    fetch(backendUrl + "messages/" + messageId + "/0", {
      method: "PUT",
      headers: {
        "Content-type": "application/json"
      }
    })
      .then(response => response.text())
      .then(data => console.log(data))
  }

  const onDislike = (messageId) => {
    // This will increment the dislikes of the given place
    fetch(backendUrl + "messages/" + messageId + "/1", {
      method: "PUT",
      headers: {
        "Content-type": "application/json"
      }
    })
      .then(response => response.text())
      .then(data => console.log(data))
  }


  return (
    <div>
      <div className="navbar-container">
        <h1 className='title'>THE BUZZ</h1>
        {
          whosLoggedIn &&
          <div>
            <h4 className="whosLoggedIn">logged in: {whosLoggedIn}</h4>
            <button onClick={() => { setWhosLoggedIn(null); setScreen('login') }}>log out</button>
          </div>
        }
      </div>
      <div className="app-container">
        {
          screen == 'login' &&
          <div className="login">
            <h2>
              We only support Google login. You need a gmail.
            </h2>
            <GoogleLogin
              clientId='26342651488-daqciiut6qikektlcu6oj6h21iij33cr.apps.googleusercontent.com'
              buttonText='Login with Google'
              onSuccess={responseGoogle}
              onFailure={responseGoogle}
              cookiePolicy={'single_host_origin'}

            />
          </div>
        }
        {
          screen == "messages" &&
          <div className="messages-container">
            <div className="add-message">
              <button onClick={() => { setScreen("addMessage") }}>Add Message</button></div>
            <table className="table">
              <thead>
                <tr>
                  <th>Subject</th>
                  <th>Messages</th>
                  <th>Likes</th>
                  <th>Dislikes</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {
                  messages.map((message) =>
                    <tr key={message.mId}>
                      <td>{message.mTitle}</td>
                      <td>{message.mContent}</td>
                      <td>{message.mLikeCount}</td>
                      <td>{message.mDislikeCount}</td>
                      <td>
                        <button className='like-button' onClick={() => { onLike(message.mId) }}>Like</button>
                        <button className='dislike-button' onClick={() => { onDislike(message.mId) }}>Dislike</button>
                        <button className='add-comment-button' onClick={() => {
                          setSelectedMessage({ id: message.mId, content: message.mContent });
                          setScreen("addComment");
                        }}>Add Comment</button>
                        <button onClick={() => {
                          setSelectedMessage({ id: message.mId, content: message.mContent });
                          getMessageComments();
                          setScreen("showComments");
                        }}>Show Comments</button>
                      </td>
                    </tr>
                  )
                }
              </tbody>
            </table>
          </div>
        }
        {
          screen === "addMessage" &&
          <div className="add-message-container">
            <h1>Add Message</h1>
            <div>
              <div>
                <div>Subject</div>
                <input onChange={(e) => { onSubjectChange(e.target.value) }} className="subject"></input>
              </div>
              <div>Message</div>
              <textarea onChange={(e) => { onMessageChange(e.target.value) }} className="message-text-area"></textarea>
            </div>
            <button onClick={() => { postMessage() }} className="add-button">Add</button>
            <button onClick={() => { setScreen("messages"); clearMessageForm(); }}>Cancel</button>
          </div>
        }
        {
          screen === "addComment" &&
          <div className="add-comment-container">
            <h1>Add Comment</h1>
            <div>
              <p>{selectedMessage.content}</p>
              <textarea onChange={(e) => { onCommentChange(e.target.value) }} className="comment-text-area"></textarea>
            </div>
            <button onClick={() => { postComment() }} className="add-button">Add</button>
            <button onClick={() => { setScreen("messages"); clearCommentForm(); }}>Cancel</button>
          </div>
        }
        {
          screen === "showComments" &&
          <div className="show-comments-container">
            <h1>Comments</h1>
            <div>
              <p>{selectedMessage.content}</p>
              {
                currentMessageComments.length > 0 &&
                currentMessageComments.map((comment, i) =>
                  <ol>
                    <li>{comment.content}</li>
                  </ol>
                )
              }
              {
                currentMessageComments == 0 &&
                <p>No comments for this message.</p>
              }
            </div>
            <button onClick={() => { setScreen("messages"); setCurrentMessageComments([]); }}>Go Back</button>
          </div>
        }
      </div>
    </div>
  );



}

export default App;
