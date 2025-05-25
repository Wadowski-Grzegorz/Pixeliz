import {useState} from 'react';
import axios from 'axios';
import { useAuth } from "./AuthProvider";

const Login = () =>{

    const { setToken } = useAuth();

    const [credentials, setCredentials] = useState({
        email: "",
        password: ""
    });

    const [invalidCredentials, setInvalidCredentials] = useState(false);

    const handleSubmit = async (event) =>{
        event.preventDefault();
        try{
            setInvalidCredentials(false);
            const response = await axios.post(`http://localhost:9090/api/auth/login`, credentials);  
            setToken(response.data.token);
        } catch(error){
            if(error.status === 401){
                setInvalidCredentials(true);
            } else{
                console.error('Error login', error);
            }
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
                { invalidCredentials && (
                    <p className="text-error">Invalid credentials.</p>
                )}
                <button type="submit" className="mt-3">Submit</button>
            </form>
        </div>
        </>
    );
}

export default Login;