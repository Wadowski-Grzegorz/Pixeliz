import { useEffect, useRef, useState } from 'react';
import { useLocation, useParams } from 'react-router-dom';
import { useAuth } from '../Auth/AuthProvider';
import axios from 'axios';
import Modal from "../common/Modal"
import RoleAdd from '../Roles/RoleAdd'
import Contributors from '../Roles/Contributors'

const PIXEL_SIZE = 15;
const COLORS = ['#000000', '#FFFFFF', '#964B00', '#6B1D8C', '#1DEB1A', '#0C16C9', '#CADB0F', '#DB0416']

const PixelDraw = () => {
    const contextRef = useRef(null);
    const pixelsRef = useRef(null);
    const gridRef = useRef(null);

    const location = useLocation();
    const { id } = useParams();
    const { token } = useAuth();

    const [row, setRow] = useState(location.state?.sizeX || 30);
    const [column, setColumn] = useState(location.state?.sizeY || 30);

    const [pixels, setPixels] = useState(Array(row * column).fill('#FFFFFF')); // drawing of colored pixels
    const [currentColor, setCurrentColor] = useState('#FFFFFF');
    const [isDrawing, setIsDrawing] = useState(false);

    const [drawingId, setDrawingId] = useState(0);
    const [drawingName, setDrawingName] = useState("");

    const [redraw, setRedraw] = useState(false);

    const [modalSave, setModalSave] = useState(false);
    const [addedFlag, setAddedFlag] = useState(0);
    const [rolePermissionDenySave, setRolePermissionDenySave] = useState(false);

    useEffect(() => {
        const ctx = pixelsRef.current.getContext("2d");
        contextRef.current = ctx;
        
        drawPixels();
        drawGrid();
        setRedraw(false);
    }, [redraw]);

    useEffect(() =>{
        if(id){
            loadDrawing(id);
        }
    }, []);

    const drawPixels = () =>{
        const ctx = contextRef.current;
        for(let x = 0; x < row; x++){
            for(let y = 0; y < column; y++){
                ctx.fillStyle = pixels[y * row + x];
                ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }    
        }
    }

    const drawGrid = () =>{
        const ctx = gridRef.current.getContext("2d");
        ctx.strokeStyle = '#808080';
        
        for(let i = 0; i <= row; i++){
            ctx.beginPath();
            ctx.moveTo(i * PIXEL_SIZE, 0);
            ctx.lineTo(i * PIXEL_SIZE, column * PIXEL_SIZE);
            ctx.stroke();
        }

        for(let i = 0; i <= column; i++){
            ctx.beginPath();
            ctx.moveTo(0, i * PIXEL_SIZE);
            ctx.lineTo(row * PIXEL_SIZE, i * PIXEL_SIZE);
            ctx.stroke();
        }
    };

    const paintPixel = (event) => {
        const ctx = contextRef.current;
        const {offsetX, offsetY} = event.nativeEvent;
        const x = Math.floor(offsetX / PIXEL_SIZE);
        const y = Math.floor(offsetY / PIXEL_SIZE);

        const newPixels = [...pixels];
        const index = y * row + x;
        
        if(pixels[index] === currentColor){
            return;
        }

        newPixels[index] = currentColor;
        ctx.fillStyle = currentColor;

        ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

        setPixels(newPixels);
    };

    const handleMouseDown = (event) => {
        if(token){
            clickDrawingStat();
        }
        setIsDrawing(true);
        paintPixel(event);
    };

    const handleMouseUp = () => {
        setIsDrawing(false);
    };

    const handleMouseMove = (event) => {
        if(isDrawing){
            paintPixel(event);
        }
    }

    const changeColor = (color) => {
        setCurrentColor(color);
    }

    const saveDrawing = async () => {
        try {
            const response = await axios.post('http://localhost:9090/api/drawing', 
                {
                    pixels: pixels,
                    name: drawingName,
                    size_x: row,
                    size_y: column
                },
                {headers: {"Content-Type": "application/json"}}
            );
            setDrawingId(response.data.id);
        } catch(error){
            console.error('Error while saving drawing:', error);
        }
    }

    const updateDrawing = async (id) => {
        try {
            setRolePermissionDenySave(false);
            const response = await axios.put(`http://localhost:9090/api/drawing/${id}`, 
                {
                    pixels: pixels,
                    name: drawingName
                },
                {headers: {"Content-Type": "application/json"}}
            );
        } catch(error){
            if(error.response && error.response.request.status === 403){
                setRolePermissionDenySave(true);
            } else{
                console.error('Error while saving drawing:', error);
            }
        }
    }
    
    const loadDrawing = async (id) => {
        if(!id || id <= 0){
            return;
        }
        try {
            const response = await axios.get(`http://localhost:9090/api/drawing/${id}`);
            const data = response.data;
            setPixels(data.drawing.pixels);
            setDrawingName(data.drawing.name);
            setRow(data.drawing.size_x);
            setColumn(data.drawing.size_y);
            setDrawingId(data.drawing.id);
            setRedraw(true);
        } catch(error){
            console.error('Error while loading drawing:', error);
        }
    }

    const saveOrUpdateDrawing = async () => {
        if(!token){
            toggleModalSave();
            return;
        }

        if(drawingId > 0){
            updateDrawing(drawingId);
        }else{
            saveDrawing();
        }
    }

    const clickDrawingStat = async () => {
        try {
            await axios.post('http://localhost:9090/api/user/stats');
        } catch(error){
            console.error('Error while sending click information:', error);
        }
    }

    const toggleModalSave = () => {
        setModalSave(!modalSave);
    }

    const incAddedFlag = () => {
        setAddedFlag(prev => prev + 1);
    }

    return(
        <div className="flex flex-col items-center justify-center relative">
            <div className="relative">
                <canvas 
                    ref={pixelsRef}
                    height={column * PIXEL_SIZE}
                    width={row * PIXEL_SIZE}
                    onMouseDown={handleMouseDown}
                    onMouseMove={handleMouseMove}
                    onMouseUp={handleMouseUp}
                    onMouseLeave={handleMouseUp}
                    className="relative top-0 left-0 z-0"
                />

                <canvas 
                    ref={gridRef}
                    height={column * PIXEL_SIZE}
                    width={row * PIXEL_SIZE}
                    className="absolute top-0 left-0 z-[1] pointer-events-none"
                />
            </div>
            
            <div className="flex fixed items-center bottom-1 left-1/2 
                            md:left-auto md:top-1/2 md:right-1 z-[2]  
                            -translate-x-1/2 md:translate-x-0 md:-translate-y-1/2">
                <div className="flex flex-row md:flex-col items-center justify-center">
                    {COLORS.map((color) => (
                        <button 
                            key={color} 
                            onClick={() => changeColor(color)}
                            style={{ backgroundColor: color }}
                            className="!border-black !rounded-none w-8 h-8 !px-0 !py-0">
                        </button>
                    ))}
                </div>
            </div>

            <div className="flex flex-col mt-2 gap-1">
                { rolePermissionDenySave && (
                        <p className="text-error mb-2">
                            You don't have permissions to edit.
                        </p>
                    )}
                <div className="flex flex-row gap-2 pb-1">
                    <input
                        type="text"
                        value={drawingName}
                        onChange={(e) => setDrawingName(e.target.value)}
                        className="!mt-0"
                        placeholder="Name your drawing"/>
                    <button onClick={saveOrUpdateDrawing}>Save drawing</button>
                </div>
            
                { token && (
                    <div className="border-t-1 border-myBack pt-2">
                        <RoleAdd drawingId={drawingId} incAddedFlag={incAddedFlag}/>
                    </div>
                )}
            </div>

            <Modal isOpen={modalSave} onClose={toggleModalSave}>Please log in to save</Modal>
            { token && (
                <div className="flex justify-between w-full mt-30 px-5 gap-5">
                    <div className="flex flex-row items-center">
                        <Contributors drawingId={drawingId} reload={addedFlag}/>
                    </div>
                </div>
            )}
        </div>
    );
}

export default PixelDraw;