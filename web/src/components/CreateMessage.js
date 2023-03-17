import * as React from "react";
import { useState } from 'react';
import { useHistory } from 'react-router-dom';

const CreateMessage = () => {

    const [title, setTitle] = useState('');
    const [body, setBody] = useState('');
    const history = useHistory();

    const clickAdd = (e) => {
        e.preventDefault();
        console.log("Add Message");
        handleSubmit();
    }

    const handleSubmit = () => {
        if (title === "" || body === "") {
            window.alert("Error: title or message is not valid");
            return;
        }
        const idea = { title, body };
        fetch("https://thebuzzaws.herokuapp.com/messages/", {
            method: 'POST',
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify({ mTitle: idea.title, mIdea: idea.body })
        }).then(() => {
            console.log('new blog added');
        })
        console.log(idea);
        history.push('/messages');
    }

    const clickCancel = (e) => {
        e.preventDefault();
        console.log("Cancel Add");
        setBody('');
        setTitle('');
        history.push('/messages');
    }
    return (
        <form className="addElement">
            <h3>Add a New Entry</h3>
            <label>Title</label>
            <input type="text" id="newTitle" value={title} onChange={(e) => setTitle(e.target.value)} />
            <label>Message</label>
            <textarea id="newMessage" value={body} onChange={(e) => setBody(e.target.value)}></textarea>
            <button id="addButton" onClick={clickAdd}>Add</button>
            <button id="addCancel" onClick={clickCancel}>Cancel</button>
        </form>);
}

export default CreateMessage;