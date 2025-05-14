import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../Auth/AuthProvider';

export const ProtectedRoutes = () => {
    const { token } = useAuth();
    
    if(!token){
        return <Navigate to="/login"/>
    }

    return <Outlet/>
}