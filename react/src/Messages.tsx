import * as React from 'react'


/** Enforce type-checking of the state for our component */
type MessageProps = { 
    message: string
    like: number
    input: string
    };


/** Declare that the component does not have any properties */



export class Messages extends React.Component<MessageProps> {
    /** State will consist of a single string */
    state = { message: "", like: 0, input: "" };
	

    /**
     * This lambda will run any time its associated input element changes.  Its
     * job is to update the state with the value of the input element.
     */
    handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        this.setState({ input: e.target.value });
	
    }

    addLike = (_e: React.MouseEvent<HTMLButtonElement>) => {
        // NB: setState will patch the state by updating any fields that are
        //     defined in the object that it is given.
        this.setState({ like: ++this.state.like });
    }

    removeLike = (_e: React.MouseEvent<HTMLButtonElement>) => {
        // NB: setState will patch the state by updating any fields that are
        //     defined in the object that it is given.
        this.setState({ like: --this.state.like });
    }
	


    /**
     * increment the number that is stored in the state
     *
     * NB: When we want a button in JSX to call methods of our component, they
     *     will not know what `this` means. The easiest workaround is to define
     *     the method as a lambda, using the `=>` syntax.
     */
	 submit = (_e: React.MouseEvent<HTMLButtonElement>) => {
        // NB: setState will patch the state by updating any fields that are
        //     defined in the object that it is given.
		//this.setState({message: React.})
        
    }


    /** Render the component */
    render() {
        return (
            <div>
                <p>New Message</p>
                <p>Subject:</p>
                <input type="text" />
                <p>Message:</p>
                <input type="text" />
                <button onClick={this.submit}> Submit</button>
                <p>{this.state.message}{this.state.like}    
                <button onClick={this.addLike}> Like</button>
                <button onClick={this.removeLike}> Disike</button>  
                </p>
            </div>
			
        );
    }
};