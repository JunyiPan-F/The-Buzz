import React, { useEffect, useState } from 'react';
import "./style.css";
import "./table.css";
import "./TestMessages.json"

export function Trial2(){

    const [data, setData] = useState([]);
    
    const getData=()=>{
        fetch('./TestMessages.json'
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
        });
    }

    useEffect(()=>{
        getData()
    },[])

    



	//const [messages, setMessages] = useState(data);
	
    /*componentDidMount() {
        // NB: the error handling isn't perfect here
        // NB: using "no-cors" is a hack for this demo

        //This retrieves the TestMessages File
        fetch('/public/TestMessages.json', { mode: "no-cors" })

        //I have no idea what these next two lines do
            .then(response => response.json()) //What is response.json
            .then(response => {
                this.setState({
                    data: response, //Is this setting the value of data to that in the file
                                    //Did it come from response.json?
                    //These next two are saying the data was recieved successfully?
                    waiting: false,
                    error: ""
                })
            })
            .catch(error => this.setState({
                waiting: false,
                error: "" + error
            }));
    } */



        return (
            <div className="app-container">
                <table>
					<thead>
						<tr>
							<th>Message</th>
							<th>Likes</th>
                            <th>Actions</th>
						</tr>
					</thead>
					<tbody>
                        <tr>
						    <td>This is a Message</td>
						    <td>1</td>
                            <td><button>Like</button>
                            <button>Disike</button></td>
                        </tr>
						<tr>
						    <td>This is a Message2</td>
						    <td>4</td>
                            <td><button>Like</button>
                            <button>Disike</button></td>
                        </tr>
						<tr>
						    <td>This is a Message6</td>
						    <td>20</td>
                            <td><button>Like</button>
                            <button>Disike</button></td>
                        </tr>
					</tbody>
				</table>
            </div>
			
        );
    

    /** I want this class to export to App a table that shows the messages and their likes
     * 
     */
};