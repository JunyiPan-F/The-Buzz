import React, { useState } from 'react';
import "./TestMessages.json"
import "./style.css";
import "./table.css";

export function newMessage(){


	const [data, setData] = useState([]);
    
    

	//https://www.pluralsight.com/guides/fetch-data-from-a-json-file-in-a-react-app
    const getData=()=>{
        fetch('TestMessages.json'
        ,{
            headers : {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }
        )
        .then(function(response){
            console.log(response)
            return response.json();
        })
        .then(function(myJson){
            console.log(myJson);
			setData(myJson);
        });
    }


    const [addFormData, setAddFormData] = useState({
		message: ' ',
		likes: 0
	})

	const handleAddFormChange = (event: any) => {
		event.preventDefault();
		const fieldName = event.target.getAttribute('message');
		const fieldValue = event.target.value;

		const newFormData: any = {...addFormData};
		newFormData[fieldName] = fieldValue;

		setAddFormData(newFormData);

	}

	const handleAddFormSubmit = (event: any) => {
		event.preventDefault();

		const newMessage = {
			message: addFormData.message,
			likes: 0
		}

		const newMessages = {...data, newMessage}
	}

    return (
		<div className = "newMess">
			<form>
				<input
				type = "text"
				name = "message"
				placeholder = "Enter message here..."
				onChange = {handleAddFormChange} 
				>
				</input>
			</form>
			<button type = "submit">Done</button>
		</div>

    /** I want this class to export to App a table that shows the messages and their likes
     * 
     */
	)
	
}