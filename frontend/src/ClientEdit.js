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
        link: '',                    //Added for new task => reference to other user
        managerID: '',
        parent_id: '',
    };

    constructor(props) {
        super(props);
        this.state = {
            item: this.emptyItem,          //initial state = object with NO name, email created above
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
        this.setState({error});
    }

    /*
    In handeSubmit, we'll call our API, sending the request to a PUT or POST method
    depending on the feature we're invoking. For that, we can check if the id property is filled:
    */

    async handleSubmit(event) {
        event.preventDefault();
        const {item} = this.state;

        //RESPONSE FROM BACK-END
        const response = await fetch('/clients' + (item.id ? '/' + item.id : ''), {
            method: (item.id) ? 'PUT' : 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(item),
        }).then((response) => response.json())
            .then((data) => {
                console.log("Result:", data);
            })
            .catch((error) => {
                console.error("Error:", error);
            });
       //  //IF - ELSE  (for response from back-end)
       //  if(!response.ok){
       //      // this.props.history.push('/clients' + (item.id ? '/' + item.id : '');
       //      console.log("Something went wrong, status code: " + response.status + "Issue: " + JSON.stringify(response.headers));
       //  }else{
       //      this.props.history.push('/clients');
       //      console.log("Successful operation, status code: " + response.status);
       //  }
       // // this.props.history.push('/clients'); //push => method adds one or more elements to the end of an array and returns the new length of the array
    }

    render() {
        const {item} = this.state;
        const title = <h2>{item.id ? 'Edit Client' : 'Add Client'}</h2>;

        return <div>
            <AppNavbar/>
            <Container>
                {title}
                <Form onSubmit={this.handleSubmit}>
                    <Label for="errorText" value={item.errorText}></Label>

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

                    {/*<FormGroup>*/}
                    {/*    <Label for="link">Link to other user ID (if no link => enter 0)</Label>*/}
                    {/*    <Input type="text" name="link" id="link" value={item.link || ''}*/}
                    {/*           onChange={this.handleChange} autoComplete="link"/>*/}
                    {/*</FormGroup>*/}

                    {/*<FormGroup>*/}
                    {/*    <Label for="manager">Link to manager</Label>*/}
                    {/*    <Input type="text" name="managerID" id="managerID" value={item.managerID || ''}*/}
                    {/*           onChange={this.handleChange} autoComplete="managerID"/>*/}
                    {/*</FormGroup>*/}

                    <FormGroup>
                        <Label for="parent_id">Link to parent ID (if no parent => enter 0)</Label>
                        <Input type="text" name="parentID" id="parentID" value={item.parentID || ''}
                               onChange={this.handleChange} autoComplete="parent_id"/>
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