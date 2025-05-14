import Navbar from "./components/Navbar/Navbar.jsx"
import MyRoutes from "./components/Routes";
import { BrowserRouter } from "react-router-dom";

function App() {
  return (
    <BrowserRouter>
      <Navbar/>

      <div className="app">
        <MyRoutes/>
      </div>
      
    </BrowserRouter>
  );
}

export default App;
