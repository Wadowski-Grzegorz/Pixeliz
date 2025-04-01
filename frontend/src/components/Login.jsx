import {useState} from 'react';
import axios from 'axios';


const Login = () =>{

    const [credentials, setCredentials] = useState({
        login: "",
        email: "",
        password: ""
    });

    const handleSubmit = (event) =>{
        event.preventDefault();
        axios.post(`http://localhost:9090/api/auth/login`, credentials)
            .then(response => console.log(response))
            .catch(error => console.error('Error login', error))
    }

    const handleChange = (event) =>{
        setCredentials({...credentials, [event.target.name]: event.target.value});
    }


    return(
        <>
            <h1>Login</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Login:
                    <input  type="text"
                            name="login"
                            value={credentials.login}
                            onChange={handleChange}/>
                </label>
                <label>
                    Email:
                    <input  type="email"
                            name="email"
                            value={credentials.email}
                            onChange={handleChange}/>
                </label>
                <label>
                    Password:
                    <input  type="password"
                            name="password"
                            value={credentials.password}
                            onChange={handleChange}/>
                </label>
                <button type="submit">Submit</button>
            </form>
        </>
    );
}

export default Login;