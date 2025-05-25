import { createContext, useContext, useEffect, useMemo, useState } from 'react';
import axios from 'axios';

const AuthContext = createContext();

const AuthProvider = ({ children }) => {
    const [token, setToken_] = useState(localStorage.getItem("token"));
    const [userName, setUserName] = useState("");

    const setToken = (newToken) => {
        setToken_(newToken);
    }

    useEffect(() => {
        if(token){
            axios.defaults.headers.common["Authorization"] = "Bearer " + token;
            localStorage.setItem('token', token);
            setUserSummary();
        } else{
            delete axios.defaults.headers.common["Authorization"];
            localStorage.removeItem('token');
        }
    }, [token]);

    const setUserSummary = async () => {
        try{
            const response = await axios.get('http://localhost:9090/api/user/summary');
            setUserName(response.data.name);
        }
        catch(error){
            console.error('Get user summary error', error);
        }
    }

    const contextValue = useMemo(
        () => ({
            token,
            setToken,
            userName
        }),
        [token, userName]
    );

    return(
        <AuthContext.Provider value={contextValue}>
            {children}
        </AuthContext.Provider>
    );
}

export const useAuth = () => {
    return useContext(AuthContext);
}

export default AuthProvider;