import { Navigate, Outlet } from 'react-router-dom';
import { useAuth } from '../Auth/AuthProvider';

export const PublicOnlyRoutes = () => {
    const { token } = useAuth();

    if(token){
        return <Navigate to="/"/>
    }

    return <Outlet/>
}