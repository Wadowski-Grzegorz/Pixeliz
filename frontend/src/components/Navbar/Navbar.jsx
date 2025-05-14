import React from 'react';
import {useState} from 'react';
import { Link } from 'react-router-dom';
import PixelCreateForm from '../PixelDraw/PixelCreateForm';


function Navbar(){
    const [formSeen, setFormSeen] = useState(false);
    const togglePop = () =>{
        setFormSeen(!formSeen);
    };


    return(
        <>
            <nav className="navbar">
                <div>
                    <Link to="/">Home</Link>
                    <button onClick={togglePop}>New Project</button>
                </div>

                <div>
                    <Link to="/resources">Resources</Link>
                    <Link to="/login">Login</Link>
                    <Link to="/register">Register</Link>
                </div>
            </nav>

            <div>
                {
                    formSeen && 
                    (
                        <>
                            <div className="popup-back"/>
                            <div className="popup">
                                <PixelCreateForm togglePop={togglePop}/>
                            </div>
                        </>
                    )
                }
            </div>
        </>
    );
}

export default Navbar;