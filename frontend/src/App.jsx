import React, { useEffect, useState } from "react";
import axios from "axios";
import ColorGrid from "./components/ColorGrid";
import PixelDraw from "./components/pixelDraw";
import Changed from "./components/changed";

function App() {
  // const [users, setUsers] = useState([]);

  // useEffect(() => {
  //   axios.get("http://localhost:8080/api/users")
  //     .then(response => setUsers(response.data))
  //     .catch(error => console.error("Error fetching users:", error));
  // }, []);

  return (
    <>
      {/* <div>
        <h1>Lista użytkowników</h1>
        <ul>
          {users.map(user => (
            <li key={user.id}>{user.name} - {user.email}</li>
          ))}
        </ul>
      </div> */}

      <ColorGrid/>
      <PixelDraw/>
      <Changed/>
    </>
  );
}

export default App;
