import * as React from "react";
import { useEffect, useState } from "react";
import { useHistory } from 'react-router-dom';

const AllMessages = () => {

    const [ideas, setIdeas] = useState(null);
    const history = useHistory();

    const clickAddIdea = () => {
        console.log("New Message");
        history.push('/create');
    }

    const clickUpVote = (e) => {
        let id = e.target.value;
        fetch("https://thebuzzaws.herokuapp.com/messages/" + { id }).then(res => {
            if (!res.ok) {
                throw Error('could not fetch data');
            }
            return res.json();
        }).then(data => {
            let likes = data.mLike + 1;
            fetch("https://thebuzzaws.herokuapp.com/messages/" + { id }, {
                method: 'PUT',
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ mLikes: likes })
            }).then(() => {
                console.log('like updated');
            })
        }).catch(err => {
            if (err.name === 'AbortError') {
                console.log('fetch aborted');
            } else {
                console.log('print other certain error');
            }
        });
    }

    const clickDownVote = (e) => {
        let id = e.target.value;
        fetch("https://thebuzzaws.herokuapp.com/messages/" + { id }).then(resp => {
            if (!resp.ok) {
                throw Error('could not fetch data');
            }
            return resp.json();
        }).then(data => {
            let likes = data.mLike - 1;
            fetch("https://thebuzzaws.herokuapp.com/messages/" + { id }, {
                method: 'PUT',
                headers: { "Content-Type": "application/json" },
                body: JSON.stringify({ mLikes: likes })
            }).then(() => {
                console.log('like updated');
            })
        }).catch(er => {
            if (er.name === 'AbortError') {
                console.log('fetch aborted');
            } else {
                console.log('print other certain error');
            }
        });
    }



    useEffect(() => {
        const abortCont = new AbortController();

        fetch("https://thebuzzaws.herokuapp.com/messages/", { signal: abortCont.signal }).then(res => {
            if (!res.ok) {
                throw Error('could not fetch data');
            }
            return res.json();
        }).then(data => {
            console.log(data);
            setIdeas(data.mData);
        }).catch(err => {
            if (err.name === 'AbortError') {
                console.log('fetch aborted');
            } else {
                console.log('print other certain error');
            }
        });

        return () => abortCont.abort();
    }, [ideas]);

    const clickDeleteIdea = () => {
        console.log("Delete idea");
    }

    const clickEditIdea = (e) => {
        console.log("Edit idea");
        history.push("/edit/" + e.target.value);
    }


    return (
        <div className="allIdeas">

            <h2 id="allIdeasHeader">All Ideas <button onClick={clickAddIdea} id="newIdeaButton">Add Idea</button> </h2>
            {ideas && ideas.map((idea) => (
                <div id="ideaList" key={idea.mId}>

                    <h3>{idea.mTitle}</h3>
                    <p>{idea.mIdea}</p>
                    <p>{idea.mLikes}</p>
                    <button onClick={clickEditIdea} className='editbtn' value={idea.mId} >Edit</button>
                    <button onClick={clickDeleteIdea} className='delbtn' value={idea.mId}>Delete</button>
                    <button onClick={clickUpVote} className='voteUp' value={idea.mId}>UpVote</button>
                    <button onClick={clickDownVote} className='voteDown' value={idea.mId}>DownVote</button>
                </div>
            ))}

        </div>);
}



export default AllMessages; 