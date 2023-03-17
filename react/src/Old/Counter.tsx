import * as React from 'react'

/** The Counter component allows the user to interact with local state */
export class Counter extends React.Component {
    /**
     * It is tempting to create our state by assigning fields to the `Counter`
     * type.  In React, that doesn't work.  We need a special `state` variable,
     * and we need to put all our fields in it.
     */
    state = { num: 0 }

    /**
     * increment the number that is stored in the state
     *
     * NB: When we want a button in JSX to call methods of our component, they
     *     will not know what `this` means. The easiest workaround is to define
     *     the method as a lambda, using the `=>` syntax.
     */
    increment = (_e: React.MouseEvent<HTMLButtonElement>) => {
        // NB: setState will patch the state by updating any fields that are
        //     defined in the object that it is given.
        this.setState({ num: ++this.state.num });
    }

    /**
     * Render the component.  We use one-way binding to display the current
     * value, and we use one-way binding to connect the button's click handler
     * to a lambda in this class
     */
    render() {
        return (
            <div>
                <p>The current value is {this.state.num}.</p>
                <button onClick={this.increment}> Update</button>
            </div>
        );
    }
};