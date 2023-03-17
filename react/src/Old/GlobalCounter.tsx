import * as React from 'react'

/**
 * The GlobalCounter has two properties: lambdas for getting and setting the
 * value
 */
type GlobalCounterProps = {
    getNum: () => number;
    setNum: (num: number) => void;
}

/** The GlobalCounter component allows the user to interact with global state */
export class GlobalCounter extends React.Component<GlobalCounterProps> {
    /** increment the number that is stored in the global state */
    increment = (_e: React.MouseEvent<HTMLButtonElement>) => {
        this.props.setNum(1 + this.props.getNum());
    }

    /**
     * Render the component.  Notice how calls to getNum are one-way bound, but
     * update when the button is clicked. 
     */
    render() {
        return (
            <div>
                <p>
                    The current value is {this.props.getNum()}.
                </p>
                <button onClick={this.increment}> Update</button>
            </div>
        );
    }
}