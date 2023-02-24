import React, { useState } from "react";
import axios from "axios";
import Swal from "sweetalert2";
import { useNavigate } from "react-router-dom";

const url = "http://localhost:8080/user/login";

export const Login = () => {
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const navigate = useNavigate();

  const hadleSubmit = (e) => {
    e.preventDefault();
    axios.post(url, loginBody).then((res) => {
      Swal.fire({
        icon: 'success',
        title: 'Hola ' + res.data['userName'],
        showConfirmButton: false,
        timer: 1500
      }).then(() => {
        localStorage.setItem('token', res.data['token'])
        localStorage.setItem('loggedUser', res.data.userName)
        navigate(`/user/${username}`);
        window.location.reload()
      })
    }).catch((error) => {
      Swal.fire({
        icon: 'error',
        title: error.response['data']['message'],
        showConfirmButton: false,
        timer: 1500
      })
    })
  }

  const loginBody = {
    username: username,
    password: password
  }

  return (
    <div id="loginBody">
      <div id="loginFormulario">
        <h1>Login</h1>
        <form onSubmit={hadleSubmit}>
          <label>UserName</label><br />
          <input value={username} onChange={(e) => setUsername(e.target.value)} type="text" name="" id="loginUsername" /><br /><br />
          <label>Password</label><br />
          <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" name="" id="loginPassword" /><br /><br />
          <input type="submit" value="Iniciar sesión" id="submitSaveImg"/>
        </form><br />
        <p>¿No tienes cuenta? <button onClick={() => navigate(`/register`)} id="rb">Registrate</button></p>
      </div>
    </div>
  );
}

export default Login;