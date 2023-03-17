import * as React from "react";
import { useParams, useHistory } from "react-router-dom";
import { useState, useEffect } from "react";

const EditMessage = (props) => {

    const { iD } = useParams().id;
    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const history = useHistory();

    useEffect(() => {
        const abortCont = new AbortController();

        fetch("https://thebuzzaws.herokuapp.com/messages/7", { signal: abortCont.signal }).then(res => {
            if (!res.ok) {
                throw Error('could not fetch data');
            }
            return res.json();
        }).then(data => {
            console.log(data);
            const idea = data.mData;
            setTitle(idea.mTitle);
            setBody(idea.mIdea);
            console.log(idea);
        }).catch(err => {
            if (err.name === 'AbortError') {
                console.log('fetch aborted');
            } else {
                console.log('print other certain error');
            }
        });

        return () => abortCont.abort();
    }, []);

    const clickUpdate = (e) => {
        e.preventDefault();
        console.log("Edited Idea");
        handleSubmit();
        console.log(iD);
    }

    const clickCancel = (e) => {
        e.preventDefault();
        console.log("Cancelled Edit");
        setBody('');
        setTitle('');
        history.push('/messages');
    }

    const handleSubmit = () => {
        if (title === "" || body === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        const idea = { title, body };
        fetch("https://thebuzzaws.herokuapp.com/messages/7", {
            method: 'PUT',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mTitle: idea.title, mIdea: idea.body })
        }).then(() => {
            console.log('new blog added');
        })
        console.log(idea);
        history.push('/messages');
    }



    return (
        <form className="editElement">
            <h3>Edit Idea</h3>
            <label>Title</label><br></br>
            <input type="text" id="editTitle" value={title} onChange={(e) => setTitle(e.target.value)} /><br></br>
            <label>Idea</label><br></br>
            <textarea id="editIdea" value={body} onChange={(e) => setBody(e.target.value)}></textarea><br></br>
            <button onClick={clickUpdate} id="editButton">Update</button>
            <button onClick={clickCancel} id="cancelEdit">Cancel</button>
            <p>{title}</p>
            <p>{body}</p>
        </form>);
}

export default EditMessage;