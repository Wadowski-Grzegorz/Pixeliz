import { useEffect, useState } from 'react';
import axios from 'axios';
import Modal from "../common/Modal"
import { useAuth } from "../Auth/AuthProvider";

const RoleAdd = ({ drawingId, incAddedFlag}) => {
    const [roles, setRoles] = useState([]);
    const [formData, setFormData] = useState({
        name: "",
        roleId: ""
    })
    const [modalOpen, setModalOpen] = useState(false);
    const [userNotFound, setUserNotFound] = useState(false);
    const [rolePermissionDenyModerate, setRolePermissionDenyModerate] = useState(false);
    const { token } = useAuth();


    const getRoles = async () => {
        try {
            const response = await axios.get('http://localhost:9090/api/role');
            setRoles(response.data);
        } catch(error){
            console.error('Error while fetching roles:', error);
        }
    }

    const addUserToDrawing = async () => {
        try {
            setUserNotFound(false);
            setRolePermissionDenyModerate(false);
            await axios.post(`http://localhost:9090/api/drawing/${drawingId}/user`, 
                formData,
                {headers: {"Content-Type": "application/json"}}
            );
            incAddedFlag();
            
        } catch(error){
            if(error.response && error.response.status === 404){
                setUserNotFound(true);
            } else if(error.response.status === 403 && error.response.data.details.admin == false){
                setRolePermissionDenyModerate(true);
            } else{
                console.error('Error adding user to drawing:', error);
            }
        }
    }

    useEffect(() => {
        if(token){
            getRoles();
        }
    }, [token]);

    const handleChange = (event) => {
        setFormData({ ...formData, [event.target.name]: event.target.value });
    }

    const toggleModalOpen = () =>{
        setModalOpen(!modalOpen);
    }

    const addUser = (event) => {
        event.preventDefault();
        if(!drawingId){
            toggleModalOpen();
            return;
        }

        addUserToDrawing();
    }

    return(
        <>
            <div>
                <form onSubmit={addUser} className="flex flex-col gap-1">
                    <label className="!my-0">
                    { userNotFound && (
                        <p className="text-error mb-2">
                            User not found.
                        </p>
                    )}
                    { rolePermissionDenyModerate && (
                        <p className="text-error mb-2">
                            You don't have permissions to moderate.
                        </p>
                    )}
                    <input  type="text" 
                            placeholder="Username"
                            name="name" 
                            value={formData.name} 
                            onChange={handleChange}
                            className="!mt-0"/>
                    </label>
                    <div className="flex justify-between">
                        {roles.map((r) => (
                            <label key={r.id}>
                                <input  type="radio" 
                                        name="roleId"
                                        value={r.id}
                                        checked={formData.roleId === String(r.id)}
                                        onChange={handleChange}
                                        className=""/>
                                {r.name}
                            </label>
                        ))}
                    </div>
                    <button type="submit" className="">Add user to drawing</button>
                </form>
                <Modal isOpen={modalOpen} onClose={toggleModalOpen}>Please save drawing to add users</Modal>
            </div>
        </>
    );
}

export default RoleAdd;