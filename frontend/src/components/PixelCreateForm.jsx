import {useState, useEffect} from 'react';


const PixelCreateForm = ({onSubmit, togglePop}) => {

    const [formData, setFormData] = useState({drawName: "", sizeX: "", sizeY: ""});

    const handleSubmit = (event) => {
        event.preventDefault();
        onSubmit(formData);
        togglePop();
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