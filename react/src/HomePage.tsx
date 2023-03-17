import * as React from 'react'
import "./HomePage.css";

/**General Questions
 * What does "One-Way" Binding mean
 */

/**Properties of the Homepage - the default will be Welcome to the Buzz but somethig else can be sent in */
type WelcomeMessage = {
    /** The only property we will have is a message */
    message: string;
}

/**
 * Creates the HomePage Class that will only output one line
 */
export class HomePage extends React.Component<WelcomeMessage> {
    /** This is how we declare default values for the properties */
    static defaultMessage = { message: "Welcome to The Buzz" };

    /**
     * "{}" syntax to read fields of class, using "one-way" binding.
     */
    render() { return <h1>Welcome to The Buzz</h1>; }
}