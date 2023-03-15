import React, { Component } from 'react';
import { Button, ButtonGroup, Container, Table } from 'reactstrap';
import AppNavbar from './AppNavbar';
import { Link } from 'react-router-dom';

/*
This component fetches manager data from endpoint '/managers', shows this data in tabular form
 */

class TopManagerList extends Component {

    constructor(props) {
        super(props);
        this.state = {managers: []};     //initial state is empty list
    }


    //ComponentDidMount function is calling our API to load our client list that is converted in JSON and
    //set as a new state of 'clients'
    componentDidMount() {
        fetch('/managers/top')                        //receiving data from 'managers' endpoint
            .then(response => response.json())        //function: 'response' is an argument(data from endpoint), argument should be converted to JSON
            .then(data => this.setState({managers: data})); //function: 'data' is an argument (received data), it set as state to 'clients'
    }


    //In addition, we'll create the render function, which will render the HTML with Edit, Delete,
    // and Add Client actions:

    render() {
        const {managers, isLoading} = this.state;         //new const = updated manager list

        if (isLoading) {
            return <p>Loading...</p>;
        }

        //managerList is a table (without headers) of managers

        const managerList = managers.map(manager => {      //going through the list one by one and adding data to table rows
            return <tr key={manager.id}>
                <td style={{whiteSpace: 'nowrap'}}>{manager.id}</td>
                <td>{manager.name}</td>
                <td>{manager.department}</td>
            </tr>
        });

        //visual part to be displayed (data goes to table, headers added)

        return (
            <div>
                <AppNavbar/>
                <Container fluid>
                    <div className="float-right">
                        <Button color="success" tag={Link} to="/managers/top">See top managers</Button>
                    </div>
                    <h3>Managers</h3>
                    <Table className="mt-4">
                        <thead>
                        <tr>
                            <th width="20%">Id</th>
                            <th width="40%">Name</th>
                            <th width="40%">Department</th>
                        </tr>
                        </thead>
                        <tbody>
                        {managerList}
                        </tbody>
                    </Table>
                    <Button color="secondary" tag={Link} to="/managers">Go back to all managers</Button>
                </Container>
            </div>
        );
    }

}
export default TopManagerList;

