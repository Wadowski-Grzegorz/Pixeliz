import { useState } from 'react';
import axios from 'axios';
import { useAuth } from "./AuthProvider";

const Register = () =>{
    const { setToken } = useAuth();

    const [credentials, setCredentials] = useState({
        name: "",
        login: "",
        email: "",
        password: ""
    });

    const handleSubmit = (event) =>{
        event.preventDefault();
        axios.post(`http://localhost:9090/api/auth/register`, credentials)
            .then(response => setToken(response.data.token))
            .catch(error => console.error('Error creating an user.', error));
    }

    const handleChange = (event) =>{
        setCredentials({...credentials, [event.target.name]: event.target.value});
    }


    return(
        <>
        <div className="flex flex-col items-center relative left-[50%] translate-x-[-50%]">
            <h1 className="mb-2 font-bold">Register</h1>
            <form onSubmit={handleSubmit}>
                <label>
                    Username:
                    <input  type="text"
                            name="name"
                            value={credentials.name}
                            onChange={handleChange}/>
                </label>
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
                <button type="submit" className="mt-3">Submit</button>
            </form>
        </div>
        </>
    );
}

export default Register;