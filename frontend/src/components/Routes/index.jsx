import { Route, Routes } from "react-router-dom";
import PixelDraw from "../PixelDraw/PixelDraw.jsx";
import PixelResources from "../PixelResources/PixelResources.jsx";
import Login from "../Auth/Login.jsx"
import Register from "../Auth/Register.jsx"
import { ProtectedRoutes } from "./ProtectedRoutes.jsx";
import { PublicOnlyRoutes } from "./PublicOnlyRoutes.jsx";

const MyRoutes = () => {

    return(
        <Routes>
          <Route element={<ProtectedRoutes/>}>
            <Route path="/drawing/:id" element={<PixelDraw/>}/>
            <Route path="/resources" element={<PixelResources/>}/>
          </Route>


            <Route element={<PublicOnlyRoutes/>}>
              <Route path="login" element={<Login/>}/>
              <Route path="register" element={<Register/>}/>
            </Route>

          <Route path="/" element={<PixelDraw/>}/>
        </Routes>
    );
}

export default MyRoutes;