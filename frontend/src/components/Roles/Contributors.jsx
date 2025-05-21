import { useEffect, useState } from 'react';
import axios from 'axios'

const Contributors = ({ drawingId, reload }) => {
    const [contributors, setContributors] = useState([]);

    const getContributors = async () => {
        try {
            const response = await axios.get(`http://localhost:9090/api/drawing/${drawingId}/user`);
            setContributors(response.data);
        } catch(error){
            console.error('Error while fetching contributors:', error);
        }
    }

    useEffect(() => {
        if(drawingId){
            getContributors();
        }
    }, [drawingId, reload]);
    
    return (
        <div className="overflow-x-auto">
            <table className="border border-myFinish shadow-md rounded">
                <thead className="bg-myBack text-left">
                    <tr>
                        <th className="border-b border-myFinish px-4 py-2 text-myTextHeavy">Username</th>
                        <th className="border-b border-myFinish px-4 py-2 text-myTextHeavy">Role</th>
                    </tr>
                </thead>
                <tbody>
                    {contributors.map((c) => (
                        <tr key={c.userId} className="hover:bg-gray-50">
                            <td className="border-b border-myFinish px-4 py-2">{c.name}</td>
                            <td className="border-b border-myFinish px-4 py-2">{c.role.name}</td>
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}
    

export default Contributors;