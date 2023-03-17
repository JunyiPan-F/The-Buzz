import * as React from 'react'
import { RouteComponentProps } from "react-router-dom";

/**
 * As with Hello.tsx, we want properties passed through the route to be strongly 
 * typed, so we create a type to describe them.
 */
type UrlProps = { num: string };

/**
 * The Properties of this component will have a `match` field, which will give
 * us access to the UrlProps
 */
export class Url extends React.Component<RouteComponentProps<UrlProps>> {
    /** When we render this component, we print the argument from the URL */
    render() {
        return (
            <div>
                <h1>Url Property Example</h1>
                <p>
                    This component can use the last part of the address as a
                    variable by reading it from props.
                </p>
                <p>The argument from the Url is: {this.props.match.params.num}</p>
            </div>
        );
    }
}