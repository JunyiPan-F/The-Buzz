import * as React from 'react'

/** Enforce type-checking of the state for our component */
type TwoWayState = { name: string };

/** Declare that the component does not have any properties */
type TwoWayProps = undefined;

/**
 * Demonstrate two-way binding by having the component state match the value of
 * an input box */
export class TwoWay extends React.Component<TwoWayProps, TwoWayState> {
    /** State will consist of a single string */
    state = { name: "" }

    /**
     * This lambda will run any time its associated input element changes.  Its
     * job is to update the state with the value of the input element.
     */
    handleChange = (e: React.ChangeEvent<HTMLInputElement>) => {
        this.setState({ name: e.target.value });
    }

    /** Render the component */
    render() {
        return (
            <div>
                <p>The value is {"{"} {this.state.name} {"}"}</p>
                <input type="text" onChange={this.handleChange} value={this.state.name} />
            </div>
        );
    }
};