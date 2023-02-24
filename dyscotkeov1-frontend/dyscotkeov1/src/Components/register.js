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
    axios.post(url, registerBody).then((res) => {
      Swal.fire({
        icon: 'success',
        title: '!Enhorabuena ' + res.data.userName + ', te has registrado con éxito',
        showConfirmButton: false,
        timer: 1500
      }).then(() => navigate('/'))
    }).catch((error) => {
      setErrores(error.response.data.subErrors);
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
    <div id="registerBody">
      <div id="registerFormulario">
        <h1>Registro</h1>
        <form onSubmit={hadleSubmit}>
          <div className="gridReg">
            <div>
              <label>UserName</label><br />
              <input value={userName} onChange={(e) => setUserName(e.target.value)} type="text" name="" id="regUserName" /><br /><br />
            </div>
            <div>
              <label>Full Name</label><br />
              <input value={fullName} onChange={(e) => setFullName(e.target.value)} type="text" name="" id="regFullName" /><br /><br />
            </div>
          </div>
          <div className="gridReg">
            <div>
              <label>Password</label><br />
              <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" name="" id="regPassword" /><br /><br />
            </div>
            <div>
              <label>Verify password</label><br />
              <input value={verifyPassword} onChange={(e) => setVerifyPassword(e.target.value)} type="password" name="" id="regVerifyPassword" /><br /><br />
            </div>
          </div>
          <div className="gridReg">
            <div>
              <label>Email</label><br />
              <input value={email} onChange={(e) => setEmail(e.target.value)} type="text" name="" id="regEmail" /><br /><br />
            </div>
            <div>
              <label>Phone Number</label><br />
              <input value={phoneNumber} onChange={(e) => setPhoneNumber(e.target.value)} type="text" name="" id="regPhoneNumber" /><br /><br />
            </div>
          </div>
          <input type="submit" value="Continuar" id="submitSaveImg"/>
        </form><br />
        <p>¿Ya tienes cuenta? <button onClick={() => navigate('/')} id="rb">Inicia sesión</button></p>
        <ul>
          {
            errores.map(er => {
              return <li>{er.message}</li>
            })
          }
        </ul>
      </div>
    </div>
  );
}

export default Register;
