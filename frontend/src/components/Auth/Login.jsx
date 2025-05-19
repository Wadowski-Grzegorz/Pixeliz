import {useState} from 'react';
import axios from 'axios';
import { useAuth } from "./AuthProvider";

const Login = () =>{

    const { setToken } = useAuth();

    const [credentials, setCredentials] = useState({
        login: "",
        email: "",
        password: ""
    });

    const handleSubmit = async (event) =>{
        event.preventDefault();
        try{
            const response = await axios.post(`http://localhost:9090/api/auth/login`, credentials);  
            setToken(response.data.token);
        } catch(error){
            console.error('Error login', error);
        }
        
    }

    const handleChange = (event) =>{
        setCredentials({...credentials, [event.target.name]: event.target.value});
    }


    return(
        <>
        <div className="flex flex-col items-center relative left-[50%] translate-x-[-50%]">
            <h1 className="mb-2 font-bold">Login</h1>
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
                <button type="submit" className="mt-3">Submit</button>
            </form>
        </div>
        </>
    );
}

export default Login;