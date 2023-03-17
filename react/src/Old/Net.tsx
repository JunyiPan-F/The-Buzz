import * as React from 'react'

/** This component will fetch and display some data */
export class Net extends React.Component {
    state = {
        /** The data we fetched */
        data: [] as string[],
        /** True if waiting on network */
        waiting: true,
        /** Error message, if any */
        error: "",
        /** For toggling button visibility */
        showButton: true
    }

    /** When the component mounts, fetch data */
    componentDidMount() {
        // NB: the error handling isn't perfect here
        // NB: using "no-cors" is a hack for this demo
        fetch('/public/courses.json', { mode: "no-cors" })
            .then(response => response.json())
            .then(response => {
                this.setState({
                    data: response,
                    waiting: false,
                    error: ""
                })
            })
            .catch(error => this.setState({
                waiting: false,
                error: "" + error
            }));
    }

    /** Add one class to the array of data */
    addClass = (_e: React.MouseEvent<HTMLButtonElement>) => {
        this.setState({ data: [...this.state.data, "CSE 440"] });
        this.setState({ showButton: false })
    }

    /** This render method will do some conditional rendering */
    render() {
        if (this.state.waiting)
            return <div>Loading...</div>;
        else if (this.state.error !== "")
            return <div>{this.state.error}</div>;
        let button: JSX.Element;
        if (!this.state.waiting && this.state.showButton)
            button = <button onClick={this.addClass}> Update</button>;
        return <div>{this.state.data.map(d => <div key={d}>{d}</div>)}{button}</div>
    }
};