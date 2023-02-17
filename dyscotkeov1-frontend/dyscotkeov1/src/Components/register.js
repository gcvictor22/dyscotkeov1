import axios from "axios";
import React, { useState } from "react";
import { useNavigate } from "react-router-dom";
import Swal from "sweetalert2";

const url = 'http://localhost:8080/user/register'

export const Register = () => {
  const [userName, setUserName] = useState('');
  const [password, setPassword] = useState('');
  const [verifyPassword, setVerifyPassword] = useState('');
  const [email, setEmail] = useState('');
  const [phoneNumber, setPhoneNumber] = useState('');
  const [fullName, setFullName] = useState('');
  const navigate = useNavigate();

  const [errores, setErrores] = useState([]);

  const hadleSubmit = (e) => {
    e.preventDefault();
    console.log(registerBody);
    axios.post(url, registerBody).then((res) => {
      Swal.fire({
        icon: 'success',
        title: '!Enhorabuena ' + res.data.userName + ', te has registrado con éxito',
        showConfirmButton: false,
        timer: 1500
      }).then(() => navigate('/'))
    }).catch((error) => {
      setErrores(error.response.data.subErrors);
      console.log(errores);
      Swal.fire({
        icon: 'error',
        title: error.response.data.message,
        showConfirmButton: false,
        timer: 1500
      })
    })
  }

  const registerBody = {
    username: userName,
    password: password,
    verifyPassword: verifyPassword,
    email: email,
    phoneNumber: phoneNumber,
    fullName: fullName
  }

  return (
    <div>
      <h1>Registro</h1>
      <form onSubmit={hadleSubmit}>
        <label>UserName</label><br />
        <input value={userName} onChange={(e) => setUserName(e.target.value)} type="text" name="" id="regUserName" /><br /><br />
        <label>Password</label><br />
        <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" name="" id="regPassword" /><br /><br />
        <label>Verify password</label><br />
        <input value={verifyPassword} onChange={(e) => setVerifyPassword(e.target.value)} type="password" name="" id="regVerifyPassword" /><br /><br />
        <label>Email</label><br />
        <input value={email} onChange={(e) => setEmail(e.target.value)} type="text" name="" id="regEmail" /><br /><br />
        <label>Phone Number</label><br />
        <input value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} type="text" name="" id="regPhoneNumber" /><br /><br />
        <label>Full Name</label><br />
        <input value={fullName} onChange={(e) => setFullName(e.target.value)} type="text" name="" id="regFullName" /><br /><br />
        <input type="submit" value="Log in" />
      </form><br />
      <button onClick={() => navigate('/')}>Registrarme</button>
    </div>
  );
}

export default Register;
