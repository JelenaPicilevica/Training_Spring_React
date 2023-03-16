import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

/*
This component fetches client data from endpoint '/clients', shows this data in tabular form,
has additional buttons in rows with data - Edit & Delete and button 'Add Client' above the table

Below we wrote only 'remove' function and linked it with the button 'Delete'
 */

class ClientList extends Component {

    constructor(props) {
        super(props);
        this.state = {clients: []};     //initial state is empty list
        this.remove = this.remove.bind(this); //now 'this' points out to object inside 'remove' function (e.g., this.name)
    }


    //ComponentDidMount function is calling our API to load our client list that is converted in JSON and
    //set as a new state of 'clients'
    componentDidMount() {
        fetch('/clients')                        //receiving data from 'clients' endpoint
            .then(response => response.json())        //function: 'response' is an argument(data from endpoint), argument should be converted to JSON
            .then(data => this.setState({clients: data})); //function: 'data' is an argument (received data), it set as state to 'clients'
    }


    //We'll also include the remove function to handle the DELETE call to the API when we want to
    //delete a client.

    async remove(id) {
        await fetch(`/clients/${id}`, {           //receiving data from 'clients/id' endpoint
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            }
        }).then(() => {
            let updatedClients = [...this.state.clients].filter(i => i.id !== id);   //taking of client with deleted id, filter creates a new list with elements that pass a test provided by a function
            this.setState({clients: updatedClients}); //NEW LIST without deleted id
        });
    }

    //In addition, we'll create the render function, which will render the HTML with Edit, Delete,
    // and Add Client actions:

    render() {
        const {clients, isLoading} = this.state;         //new const = updated client list

        if (isLoading) {
            return <p>Loading...</p>;
        }

        //clientList is a table (without headers) of clients with 2 buttons - Edit & Delete, we will display it in 'return'

        const clientList = clients.map(client => {      //going through the list one by one and adding data to table rows
            return <tr key={client.id}>
                <td style={{whiteSpace: 'nowrap'}}>{client.id}</td>
                <td>{client.name}</td>
                <td>{client.email}</td>
                <td>{client.dob}</td>
                <td>{client.age}</td>
                <td>{client.link}</td>
                <td>{client.linkCount}</td>
                <td>{client.managerID}</td>

                <td>
                    <ButtonGroup>
                        <Button size="sm" color="primary" tag={Link} to={"/clients/" + client.id}>Edit</Button>
                        <Button size="sm" color="danger" onClick={() => this.remove(client.id)}>Delete</Button>
                    </ButtonGroup>
                </td>
            </tr>
        });

        //visual part to be displayed (button to add new clients, table header + data)

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        {/**/}
                        <Button color="success" tag={Link} to="/clients/new">Add Client</Button>
                        <Button color="success" tag={Link} to="/clients/youngest">Youngest clients</Button>
                        <Button color="success" tag={Link} to="/clients/linked">Linked clients</Button>
                        <Button color="primary" tag={Link} to="/managers">Go to managers section</Button>
                    </div>

                    <h3>Clients</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="8%">Id</th>
                            <th width="18%">Name</th>
                            <th width="18%">Email</th>
                            <th width="15%">Date of Birth</th>
                            <th width="8%">Age</th>
                            <th width="8%">Link</th>
                            <th width="8%">Link count</th>
                            <th width="8%">Manager ID</th>
                            <th width="35%">Actions</th>
                        </tr>
                        </thead>
                        <tbody>
                        {clientList}
                        </tbody>
                    </Table>
                </Container>
            </div>
        );
    }

}
export default ClientList;

