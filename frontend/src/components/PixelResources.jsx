import {useEffect, useState} from 'react';
import axios from 'axios';
import { Link } from 'react-router-dom';

//TODO:
//Click on drawing goes to /id with drawing

const PixelResources = () =>{

    const [drawings, setDrawings] = useState([]);

    // get all drawings from backend (for now not only for certain user)
    useEffect(() =>{
        axios.get(`http://localhost:9090/api/drawing`)
            .then(response =>{
                setDrawings(response.data)
            })
            .catch(error => console.error('Error fetching drawings.', error));
    }, []);

    return(
        <>
            <h1>Resources</h1>
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