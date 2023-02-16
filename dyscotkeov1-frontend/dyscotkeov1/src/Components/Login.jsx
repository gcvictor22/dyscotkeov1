import React, {useState} from "react";

export const Login = (props) => {
    const url = "http://localhost:8080/user/login"
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');

    const hadleSubmit = (e) => {
      e.preventDefault();
    }

  return (
    <div>
        <h1>Login</h1>
      <form onSubmit={hadleSubmit}>
        <label>UserName</label><br />
        <input value={email} onChange={(e) => setEmail(e.target.value)} type="text" name="" id="" /><br /><br />
        <label>Password</label><br />
        <input value={password} onChange={(e) => setPassword(e.target.value)} type="password" name="" id="" /><br /><br />
        <input type="submit" value="Log in" />
      </form><br />
      <button onClick={() => props.onFormSwitch('register')}>Registrarme</button>
    </div>
  );
}

export default Login;
