import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

/*
This component fetches client data from endpoint '/clients', shows this data in tabular form,
has additional buttons in rows with data - Edit & Delete and button 'Add Client' above the table

Below we wrote only 'remove' function and linked it with the button 'Delete'
 */

class ClientCEOByLevels extends Component {

    constructor(props) {
        super(props);
        this.state = {clients: []};     //initial state is empty list
    }


    //ComponentDidMount function is calling our API to load our client list that is converted in JSON and
    //set as a new state of 'clients'
    componentDidMount() {
        fetch('/clients/CEObyLevels')                        //receiving data from 'clients' endpoint
            .then(response => response.json())        //function: 'response' is an argument(data from endpoint), argument should be converted to JSON
            .then(data => this.setState({clients: data})); //function: 'data' is an argument (received data), it set as state to 'clients'
    }


    //In addition, we'll create the render function, which will render the HTML with Edit, Delete,
    // and Add Client actions:

    render() {
        const {clients, isLoading} = this.state;         //new const = updated client list

        if (isLoading) {
            return <p>Loading...</p>;
        }

        //clientList is a table (without headers) of clients with 2 buttons
        const clientList = clients.map(client => {      //going through the list one by one and adding data to table rows
            return <tr key={client.id}>
                <td style={{whiteSpace: 'nowrap'}}>{client.id}</td>
                <td>{client.name}</td>
                <td>{client.email}</td>
                <td>{client.dob}</td>
                <td>{client.age}</td>
                {/*<td>{client.link}</td>*/}
                {/*<td>{client.linkCount}</td>*/}
                {/*<td>{client.managerID}</td>*/}
                <td>{client.parentID}</td>
                {/*<td>{client.childCount}</td>*/}
                {/*<td>{client.levelsBelow}</td>*/}
            </tr>
        });

        //visual part to be displayed (button to add new clients, table header + data)

        return (
            <div>
                <AppNavbar/>
                <Container fluid>

                    <h3>CEO (from Client list)</h3>
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
                            {/*<th width="8%">Manager ID</th>*/}
                            <th width="8%">Parent ID</th>
                            {/*<th width="8%">Levels count</th>*/}
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
export default ClientCEOByLevels;

