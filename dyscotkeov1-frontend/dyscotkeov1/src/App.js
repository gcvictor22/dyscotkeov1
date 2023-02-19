import './App.css';
import React from 'react';
import Login from './Components/login';
import Register from './Components/register';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import Profile from './Components/profile';
import PageNotFound from './Components/notFoundPage';
import FindUsers from "./Components/findUsers";
import User from './Components/user';

function App() {

  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Login/>}></Route>
          <Route path='/register' element={<Register/>}></Route>
          <Route path='/profile' element={<Profile/>}></Route>
          <Route path='*' element={<PageNotFound/>}></Route>
          <Route path='/users' element={<FindUsers/>}></Route>
          <Route path='/user/:userName' element={<User/>}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;