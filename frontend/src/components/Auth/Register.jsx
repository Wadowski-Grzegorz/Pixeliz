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

    const [invalidCredentialsType, setInvalidCredentialsType ] = useState({
        name: false,
        login: false,
        email: false
    });

    const handleSubmit = async (event) =>{
        event.preventDefault();
        try{
            setInvalidCredentialsType({
                name: false,
                login: false,
                email: false
            });
            const response = await axios.post(`http://localhost:9090/api/auth/register`, credentials);
            setToken(response.data.token);
            
        } catch(error){
            if(error.status === 409){
                setInvalidCredentialsType((prev) =>({
                    ...prev, 
                    [error.response.data.error]: true
                }));
                console.error('Error creating an user.', error)
            } else {
                console.error('Error creating an user.', error)
            }
        }
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
                    { invalidCredentialsType.name && (
                        <p className="text-error">Taken</p>
                    )}
                    <input  type="text"
                            name="name"
                            value={credentials.name}
                            onChange={handleChange}/>
                </label>
                <label>
                    Login:
                    { invalidCredentialsType.login && (
                        <p className="text-error">Taken</p>
                    )}
                    <input  type="text"
                            name="login"
                            value={credentials.login}
                            onChange={handleChange}/>
                </label>
                <label>
                    Email:
                    { invalidCredentialsType.email && (
                        <p className="text-error">Taken</p>
                    )}
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