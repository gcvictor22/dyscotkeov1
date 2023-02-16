import React, {useState} from "react";

export const Register = (props) => {
    const url = "http://localhost:8080/user/register"
    const [userName, setUserName] = useState('');
    const [password, setPassword] = useState('');
    const [verifyPassword, setVerifyPassword] = useState('');
    const [email, setEmail] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [fullName, setFullName] = useState('');
    
    const hadleSubmit = (e) => {
      e.preventDefault();
    }

  return (
    <div>
        <h1>Registro</h1>
      <form onSubmit={hadleSubmit}>
        <label>User name</label><br />
        <input value={email} onChange={(e) => setEmail(e.target.value)} type="text" name="" id="" /><br /><br />
        <label>Password</label><br />
        <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" name="" id="" /><br /><br />
        <input type="submit" value="Log in" />
      </form><br />
      <button onClick={() => props.onFormSwitch('login')}>Registrarme</button>
    </div>
  );
}

export default Register;
