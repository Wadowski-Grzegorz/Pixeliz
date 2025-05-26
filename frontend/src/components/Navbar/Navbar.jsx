import React from 'react';
import {useState} from 'react';
import { Link } from 'react-router-dom';
import PixelCreateForm from '../PixelDraw/PixelCreateForm';
import { useAuth } from '../Auth/AuthProvider';
import Modal from '../common/Modal';

function Navbar(){
    const [formSeen, setFormSeen] = useState(false);
    const togglePop = () =>{
        setFormSeen(!formSeen);
    };

    const { token, userName, logout } = useAuth();

    return(
        <>
            <nav className="z-[100] flex flex-row justify-between items-center relative bg-[#575757] 
                        border-b-2 border-[#5E5E5E] p-[5px] mb-[10px] text-[#b8adb2]">
                <div className="flex flex-row justify-between gap-4">
                    <Link to="/">Home</Link>
                    <button onClick={togglePop} className="cursor-pointer font-bold">New Project</button>
                </div>

                { userName && (
                    <p className="absolute left-1/2 text-myTextLight -translate-x-1/2 italic">
                        {userName}
                    </p>
                )}

                <div className="flex flex-row justify-between gap-4">
                    <Link to="/resources">Resources</Link>
                    { token ? (
                        <button onClick={logout} className="font-bold">Logout</button>
                    ) : (
                        <>
                            <Link to="/login">Login</Link>
                            <Link to="/register">Register</Link>
                        </>
                    )}
                </div>
            </nav>

            <Modal isOpen={formSeen} onClose={togglePop}>
                <PixelCreateForm togglePop={togglePop}/>
            </Modal>
        </>
    );
}

export default Navbar;