import {useEffect, useState} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRotateRight } from "@fortawesome/free-solid-svg-icons";
import { useAuth } from "../Auth/AuthProvider";

const PixelResources = () =>{

    const [drawings, setDrawings] = useState([]);
    const { token } = useAuth();

    const fetchDrawings = () =>{
        axios.get(`http://localhost:9090/api/drawing/me`)
            .then(response =>{
                setDrawings(response.data)
            })
            .catch(error => console.error('Error fetching drawings.', error));
    };

    useEffect(() =>{
        if(token){
            fetchDrawings();
        }
    }, []);

    return(
        <>
            <div className="flex flex-row justify-center gap-3 mb-5">
                <h1 className="font-bold">Resources</h1>

                <button onClick={fetchDrawings} className="border-none">
                    <FontAwesomeIcon icon={faRotateRight} />
                </button>
            </div>

            <div className="flex flex-wrap gap-x-8 gap-y-4 px-4">
                {
                    drawings.map((ob) =>
                        <Link 
                            to={`/drawing/${ob.drawing.id}`} 
                            key={ob.drawing.id} 
                            className="block"
                            state={{
                                pixels: ob.drawing.pixels,
                                sizeX: ob.drawing.size_x,
                                sizeY: ob.drawing.size_y,
                                name: ob.drawing.name,
                                id: ob.drawing.id
                            }}
                        >
                            <li className="list-none flex items-center justify-center bg-[#CCCCCC] rounded-lg text-[#494446] w-25 h-25 p-lg truncate break-words hover:bg-[#9b9a9a]">
                                {ob.drawing.name || "drawing"}
                            </li>
                        </Link>
                    )
                }
            </div>
        </>
    );
}

export default PixelResources;