import React, { useEffect, useState } from 'react';
import "./style.css";
import "./table.css";
import "./TestMessages.json"

export function MessageTable(){

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

    useEffect(()=>{
        getData()
    },[])

    const buttonHandler = (event: React.MouseEvent<HTMLButtonElement>) => {
        event.preventDefault()
    
    }

    

    //fetch
    //Post

    //fetch
    //Put


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
						{data.map((dataP)=> 
							<tr>
								<td>{dataP.message}</td>
								
								<td>{dataP.likes}</td>
                                setLikes(dataP.likes)
								<td><button onClick = {buttonHandler} >Like</button>
								<button>Disike</button></td>
								
						 	</tr>)}
                        
					</tbody>
				</table>
            </div>
			
        );
    

    /** I want this class to export to App a table that shows the messages and their likes
     * 
     */
};

