import {useEffect, useState} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRotateRight } from "@fortawesome/free-solid-svg-icons";

const PixelResources = () =>{

    const [drawings, setDrawings] = useState([]);

    const fetchDrawings = () =>{
        axios.get(`http://localhost:9090/api/drawing`)
            .then(response =>{
                setDrawings(response.data)
            })
            .catch(error => console.error('Error fetching drawings.', error));
    };

    useEffect(() =>{
        fetchDrawings();
    }, []);

    return(
        <>
            <div className="flex flex-row gap-3">
                <h1 className="font-bold">Resources</h1>

                <button onClick={fetchDrawings}>
                    <FontAwesomeIcon icon={faRotateRight} />
                </button>
            </div>

            <div className="flex flex-wrap gap-x-8 gap-y-4 px-4">
                {
                    drawings.map((d) =>
                        <Link to={`/drawing/${d.id}`} key={d.id} className="block">
                            <li className="list-none flex items-center justify-center bg-[#CCCCCC] rounded-lg text-[#494446] w-25 h-25 p-lg truncate break-words hover:bg-[#9b9a9a]">
                                {d.name || "drawing"}
                            </li>
                        </Link>
                    )
                }
            </div>
        </>
    );
}

export default PixelResources;