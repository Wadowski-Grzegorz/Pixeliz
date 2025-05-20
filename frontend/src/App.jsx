import Navbar from "./components/Navbar/Navbar.jsx"
import MyRoutes from "./components/Routes";
import { BrowserRouter } from "react-router-dom";
import AuthProvider from "./components/Auth/AuthProvider.jsx";

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <div>
          <Navbar/>
          <MyRoutes/>
        </div>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
