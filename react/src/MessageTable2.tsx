import * as React from 'react';
import "./style.css";
import "./table.css";
import "./TestMessages.json"





export class MessageTable2 extends React.Component{

    state = {
		data: [] as any[]
	}

	//https://www.pluralsight.com/guides/fetch-data-from-a-json-file-in-a-react-app
    componentDidMount() {
        fetch('./TestMessages.json'
        ,{
            headers : {
                'Content-Type': 'application/json',
                'Accept': 'application/json'
            }
        }
        )
        .then(function(response){
            this.setState({
				data: response,
			})
        })
        .then(function(myJson){
            console.log(myJson);
        });
    }

	

	render() {

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
						{this.state.data.map((d)=> 
							<tr>
								<td>{d.message}</td>
								<td>{d.likes}</td>
								<td><button type = "button">Like</button>
								<button>Disike</button></td>	
						 	</tr>)}
                        
					</tbody>
				</table>
            </div>
			
        );
	}
    

    /** I want this class to export to App a table that shows the messages and their likes
     * 
     */
};

