import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

//The ClientEdit component will be responsible for creating and editing our client.

class Login extends Component {

    emptyItem = {                    //object with NO name, email, dob
        username: '',
        password: ''

    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem          //initial state = object with NO name, email created above
        };
        this.handleChange = this.handleChange.bind(this); //now 'this' points out to object inside function (e.g., this.name)
        this.handleSubmit = this.handleSubmit.bind(this); //now 'this' points out to object inside function (e.g., this.name)
    }

    /*
    Then in the handleChange function, we'll update our component state item property that will be used
    when submitting our form:
    */

    handleChange(event) {                       //TO INVESTIGATE HERE
        const target = event.target;
        const value = target.value;
        const name = target.name;
        let item = {...this.state.item};     //item state = current state
        item[name] = value;                  //changing existing name to value???
        this.setState({item});          //setting new state for item
    }


    // In handeSubmit, we'll call our API, sending the request to POST method

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;
        // console.log("Username: " + item.username);

        //RESPONSE FROM BACK-END (put it in const in order to use it in if-else)
        const response = await fetch('/signIn', {
            method : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });

        //IF - ELSE  (for response from back-end)
        if(!response.ok){
            this.props.history.push('/login');
            console.log("Login failed, status code: " + response.status);
        }else{
            this.props.history.push('/clients');
            console.log("Login successful, status code: " + response.status);
        }
    }

    render() {
        const {item} = this.state;
        const title = <h2>{'LOGIN'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>

                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="text" name="email" id="email"
                               onChange={this.handleChange}/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="password">Password</Label>
                        <Input type="text" name="password" id="password" value={item.password || ''}
                               onChange={this.handleChange} autoComplete="password"/>
                    </FormGroup>

                    <FormGroup>
                        <Button color="primary" type="submit">Submit</Button>{' '}
                        <Button color="secondary" tag={Link} to="/">Back</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }


}
export default withRouter(Login);    //Why wth Router here and in ClientList - no? Navigation