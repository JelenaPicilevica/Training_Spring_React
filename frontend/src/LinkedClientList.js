import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import {Link, withRouter} from 'react-router-dom';

class LinkedClientList extends Component {

    constructor(props) {
        super(props);
        this.state = {linkedClients: []};
    }

    //Receiving data from '/clients/youngest' endpoint in JSON and setting as new state for 'youngestClients'
    componentDidMount() {
        fetch('/clients/linked')                                 //receiving data from 'clients/youngest' endpoint
            .then(response => response.json())                          //function: 'response' is an argument(data from endpoint), argument should be converted to JSON
            .then(data => this.setState({linkedClients: data})); //function: 'data' is an argument (received data), it set as state to 'youngestClients'
    }


    //Render function will render the HTML with Edit, Delete actions:

    render() {
        const {linkedClients, isLoading} = this.state;         //new const = updated youngesr client list

        if (isLoading) {
            return <p>Loading...</p>;
        }

        //clientList is a table (without headers) of clients with 2 buttons - Edit & Delete, we will display it in 'return'

        const clientList = linkedClients.map(client => {      //going through the list one by one and adding data to table rows
            return <tr key={client.id}>
                <td style={{whiteSpace: 'nowrap'}}>{client.id}</td>
                <td>{client.name}</td>
                <td>{client.email}</td>
                <td>{client.dob}</td>
                <td>{client.age}</td>
                {/*<td>{client.link}</td>*/}
                {/*<td>{client.linkCount}</td>*/}
                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/clients/" + client.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(client.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        //visual part to be displayed (table header + data)

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    {/*<div className="float-right">*/}
                    {/*    <Button color="success" tag={Link} to="/clients/new">Add Client</Button>*/}
                    {/*</div>*/}
                    <h3>Linked clients</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="8%">Id</th>
                            <th width="18%">Name</th>
                            <th width="18%">Email</th>
                            <th width="15%">Date of Birth</th>
                            <th width="8%">Age</th>
                            {/*<th width="8%">Link</th>*/}
                            {/*<th width="8%">Link count</th>*/}
                            <th width="35%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {clientList}
                        </tbody>
                    </Table>
                    <Button color="secondary" tag={Link} to="/clients">Go back to all clients</Button>
                </Container>
            </div>
        );
    }
}
    export default withRouter(LinkedClientList);