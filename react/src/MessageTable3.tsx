import * as React from 'react';
import "./style.css";
import "./table.css";
import "./TestMessages.json"





export class MessageTable3 extends React.Component{

    state = {
		data: [] as any[],
		likes: 0
	}

	//https://www.pluralsight.com/guides/fetch-data-from-a-json-file-in-a-react-app
    componentDidMount() {
        // NB: the error handling isn't perfect here
        // NB: using "no-cors" is a hack for this demo
        fetch('/public/TestMessages.json', { mode: "no-cors" })
            .then(response => response.json())
            .then(response => {
                this.setState({
                    data: response,
                })
            })
    }


	increment = (_e: React.MouseEvent<HTMLButtonElement>) => {
        // NB: setState will patch the state by updating any fields that are
        //     defined in the object that it is given.
        this.setState({ likes: ++this.state.likes});
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
								<td><button onClick = {d.likes + 1}>Like</button> - Create onClick that calls route for messages
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

