import React, { Component } from 'react';
import './App.css';
import Home from './Home';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import ClientList from './ClientList';
import ClientEdit from "./ClientEdit";
import YoungestClientList from "./YoungestClientList";
import Login from "./Login";
import LinkedClientList from "./LinkedClientList";



class App extends Component {
    render() {
        return (
            <Router>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/clients' exact={true} component={ClientList}/>
                    <Route path='/login' component={Login}/>
                    <Route path='/clients/youngest' component={YoungestClientList}/>
                    <Route path='/clients/linked' component={LinkedClientList}/>
                    <Route path='/clients/:id' component={ClientEdit}/>
                </Switch>
            </Router>
        )
    }
}

export default App;