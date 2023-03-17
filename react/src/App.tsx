//import * as React from "react";
import * as React  from "react";
import { HashRouter as Router, Route, Link, Switch } from 'react-router-dom';
import { HomePage } from "./HomePage";
import {MessageTable3} from "./MessageTable3";
import {newMessage} from "./newMessage";
//import {MessageTable} from "./MessageTable";
import {Net} from "./Net";
 

//GOING TO NEED TO IMPORT THE DATA FROM THE API - GOING TO NEED A JSON DATAFILE

/** App has one property: a number */
type AppProps = { num: number }


export class App extends React.Component<AppProps> {

    
    
    /** The global state for this component is a counter */
    state = { num: 0 };
    

    /**
     * When the component mounts, we need to set the initial value of its
     * counter
     */
    componentDidMount = () => { this.setState({ num: this.props.num }); }

    /** Get the current value of the counter */
    getNum = () => this.state.num;

    /** Set the counter value */
    setNum = (num: number) => this.setState({ num });

    

    render() {
        return (
            <Router>
                <div className = "top">
                    The BUZZ
                </div>
                <div>
                    <nav>
                        <Link to="/">Home</Link>
                        &nbsp;|&nbsp;
                        <Link to="/messages">Messages</Link>
                        <div className = "NewMessage">
                            <Link to = "/newMessage">
                                <button type = "button">New Message</button>
                            </Link>
                        </div>
                    </nav>
                    <Switch>
                        <Route exact path="/" component={HomePage} />
                        <Route exact path="/newMessage" component={newMessage} />
                        <Route exact path="/messages" component={MessageTable3} />
                    </Switch>
                    <div className = "footer">
                        Team 7: Amazon Web Services © 2021
                    </div>
                </div>
            </Router>
        );
    }
}
