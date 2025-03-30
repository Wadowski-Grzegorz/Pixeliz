import React, { useEffect, useState } from "react";
import { BrowserRouter, Route, Routes } from "react-router-dom";
import PixelDraw from "./components/Draw/PixelDraw.jsx";
import PixelResources from "./components/PixelResources";
import Navbar from "./components/Navbar.tsx"

function App() {
  return (
    <BrowserRouter>
      <Navbar/>

      <Routes>
        <Route path="/" element={<PixelDraw/>}/>
        <Route path="/drawing/:id" element={<PixelDraw/>}/>
        <Route path="/resources" element={<PixelResources/>}/>
      </Routes>
      
    </BrowserRouter>
  );
}

export default App;
