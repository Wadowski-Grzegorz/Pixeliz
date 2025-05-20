import { useEffect, useState } from 'react';
import axios from 'axios';

const RoleAdd = ({ drawingId }) => {
    const [roles, setRoles] = useState([]);
    const [formData, setFormData] = useState({
        name: "",
        roleId: ""
    })


    const getRoles = async () => {
        try {
            const response = await axios.get('http://localhost:9090/api/role');
            setRoles(response.data);
        } catch(error){
            console.error('Error while fetching roles:', error);
        }
    }

    const addUserToDrawing = async (event) => {
        event.preventDefault();
        try {
            console.log("drawingId: ", drawingId);
            console.log("formData: ", formData);
            const response = await axios.post(`http://localhost:9090/api/drawing/${drawingId}/user`, 
                formData,
                {headers: {"Content-Type": "application/json"}}
            );
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

    return(
        <>
            <div>Add other users to your drawing</div>
            <form onSubmit={addUserToDrawing}>
                <label>
                <input  type="text" 
                        placeholder="Username"
                        name="name" 
                        value={formData.name} 
                        onChange={handleChange}/>
                </label>
                <div className="flex gap-3">
                    {roles.map((r) => (
                        <label key={r.id}>
                            <input  type="radio" 
                                    name="roleId"
                                    value={r.id}
                                    checked={formData.roleId === String(r.id)}
                                    onChange={handleChange}/>
                            {r.name}
                        </label>
                    ))}
                </div>
                <button type="submit" className="">Add</button>
            </form>
        </>
    );
}

export default RoleAdd;