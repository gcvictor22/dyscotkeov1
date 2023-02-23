import './App.css';
import React from 'react';
import Login from './Components/login';
import Register from './Components/register';
import { BrowserRouter, Route, Routes } from 'react-router-dom';
import PageNotFound from './Components/notFoundPage';
import User from './Components/user';

function App() {

  return (
    <div className="App">
      <BrowserRouter>
        <Routes>
          <Route path='/' element={<Login/>}></Route>
          <Route path='/register' element={<Register/>}></Route>
          <Route path='*' element={<PageNotFound/>}></Route>
          <Route path='/user/:userName' element={<User/>}></Route>
        </Routes>
      </BrowserRouter>
    </div>
  );
}

export default App;