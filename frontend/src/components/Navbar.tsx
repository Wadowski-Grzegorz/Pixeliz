import React from 'react';
import {useState} from 'react';
import { Link } from 'react-router-dom';
import PixelCreateForm from './Draw/PixelCreateForm';


function Navbar(){
    const [formSeen, setFormSeen] = useState(false);
    const togglePop = () =>{
        setFormSeen(!formSeen);
    };


    return(
        <>
            <nav className="navbar">
                <div>
                    <button onClick={togglePop}>New Project</button>
                </div>

                <div>
                    <Link to="/">Home</Link>
                    <Link to="/Resources">Resources</Link>
                </div>
            </nav>
            <div>
                {formSeen ? <PixelCreateForm togglePop={togglePop}/> : null}
            </div>
        </>
    );
}

export default Navbar;