import { useEffect, useState } from 'react';
import axios from 'axios';
import Modal from "../common/Modal"

const RoleAdd = ({ drawingId, incAddedFlag}) => {
    const [roles, setRoles] = useState([]);
    const [formData, setFormData] = useState({
        name: "",
        roleId: ""
    })
    const [modalOpen, setModalOpen] = useState(false);

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
            await axios.post(`http://localhost:9090/api/drawing/${drawingId}/user`, 
                formData,
                {headers: {"Content-Type": "application/json"}}
            );
            incAddedFlag();
        } catch(error){
            console.error('Error adding user to drawing:', error);
        }
    }

    useEffect(() => {
            getRoles();
    }, []);

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