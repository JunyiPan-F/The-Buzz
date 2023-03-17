import React, { useState } from 'react';
import data from "./TestMessages.json";
import "./style.css";

export function Trial(){
    
	const [messages, setMessages] = useState(data);
        return (
            <div className="app-container">
                <table>
					<thead>
						<tr>
							<th>Message</th>
							<th>Likes</th>
						</tr>
					</thead>
					<tbody>
                        {messages.map((message) => (   // How it is done in the tutorial: <div>{this.state.data.map(d => <div key={d}>{d}</div>)}{button}</div>
                        <tr>
						    <td>{message.message}</td>
						    <td>{message.likes}</td>
                        </tr>
                        ))}
                        
					</tbody>
				</table>
            </div>
			
        );
    

    /** I want this class to export to App a table that shows the messages and their likes
     * 
     */
};