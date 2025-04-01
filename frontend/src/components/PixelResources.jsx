import {useEffect, useState} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';
import { FontAwesomeIcon } from "@fortawesome/react-fontawesome";
import { faRotateRight } from "@fortawesome/free-solid-svg-icons";

//TODO:
//Click on drawing goes to /id with drawing

const PixelResources = () =>{

    const [drawings, setDrawings] = useState([]);

    // get all drawings from backend (for now not only for certain user)
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
            <div className="title">
                <h1>Resources</h1>

                <button className="button-icon" onClick={fetchDrawings}>
                    <FontAwesomeIcon icon={faRotateRight} />
                </button>
            </div>

            <div className="resources">
                {
                    drawings.map((d) =>
                        <Link to={`/drawing/${d.id}`} key={d.id} className="tile">
                            <li>{d.name || "drawing"}</li>
                        </Link>
                    )
                }
            </div>
        </>
    );
}

export default PixelResources;