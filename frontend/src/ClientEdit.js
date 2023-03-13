import React, { Component } from 'react';
import { Link, withRouter } from 'react-router-dom';
import { Button, Container, Form, FormGroup, Input, Label } from 'reactstrap';
import AppNavbar from './AppNavbar';

//The ClientEdit component will be responsible for creating and editing our client.

class ClientEdit extends Component {

    emptyItem = {                    //object with NO name, email, dob
        name: '',
        email: '',
        dob: '',
        link: ''                    //Added for new task => reference to other user
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
    Let's add the 'componentDidMount' function to check whether we're dealing with the create or
    edit feature; in the case of editing, it'll fetch our client from the API:
    */

    async componentDidMount() {
        if (this.props.match.params.id !== 'new') {
            const client = await (await fetch(`/clients/${this.props.match.params.id}`)).json();
            this.setState({item: client}); //if id is NOT new, state changes to client actual data (json)
        }
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

    /*
    In handeSubmit, we'll call our API, sending the request to a PUT or POST method
    depending on the feature we're invoking. For that, we can check if the id property is filled:
    */

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        //RESPONSE FROM BACK-END
        await fetch('/clients' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        });
        this.props.history.push('/clients'); //push => method adds one or more elements to the end of an array and returns the new length of the array
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Client' : 'Add Client'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>

                    <FormGroup>
                        <Label for="name">Name</Label>
                        <Input type="text" name="name" id="name" value={item.name || ''}
                               onChange={this.handleChange} autoComplete="name"/>
                    </FormGroup>

                    <FormGroup>
                        <Label for="email">Email</Label>
                        <Input type="text" name="email" id="email" value={item.email || ''}
                               onChange={this.handleChange} autoComplete="email"/>
                    </FormGroup>

                    {/*Added new field below*/}

                    <FormGroup>
                        <Label for="dob">Date of Birth</Label>
                        <Input type="date" name="dob" id="dob" value={item.dob || ''}
                               onChange={this.handleChange} autoComplete="dob"/>
                    </FormGroup>

                    {/*Added new field below*/}

                    <FormGroup>
                        <Label for="link">Link to other user</Label>
                        <Input type="text" name="link" id="link" value={item.link || ''}
                               onChange={this.handleChange} autoComplete="link"/>
                    </FormGroup>

                    <FormGroup>
                        <Button color="primary" type="submit">Save</Button>{' '}
                        <Button color="secondary" tag={Link} to="/clients">Cancel</Button>
                    </FormGroup>
                </Form>
            </Container>
        </div>
    }


}
export default withRouter(ClientEdit);    //Why wth Router here and in ClientList - no? Navigation