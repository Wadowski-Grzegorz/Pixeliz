import {useEffect, useState} from 'react';
import axios from 'axios';

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
            <div>
                {
                    drawings.map((d) =>
                        <div key={d.id}>{d.name}hyhy</div>
                    )
                }
            </div>
        </>
    );
}

export default PixelResources;