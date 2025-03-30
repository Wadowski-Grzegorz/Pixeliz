import { useState } from 'react';
import { useNavigate } from 'react-router-dom';


const PixelCreateForm = ({togglePop}) => {

    const [formData, setFormData] = useState({drawName: "", sizeX: "", sizeY: ""});
    const navigate = useNavigate();

    const handleSubmit = (event) => {
        event.preventDefault();
        togglePop();
        navigate("/", {state: formData});
        window.location.reload();
    }

    const handleChange = (event) => {
        setFormData({...formData, [event.target.name]: event.target.value});
    }

    return(
        <>
            <form onSubmit={handleSubmit}>
                <label>
                    Drawing name:
                    <input  type="text" 
                            name="drawName" 
                            value={formData.drawName} 
                            onChange={handleChange}/>
                </label>
                <label>
                    Size:
                    <input type="number" name="sizeX" value={formData.sizeX} onChange={handleChange}/>
                    <input type="number" name="sizeY" value={formData.sizeY} onChange={handleChange}/>
                </label>
                <button type="submit">Submit</button>
            </form>
        </>
    );
}

export default PixelCreateForm;