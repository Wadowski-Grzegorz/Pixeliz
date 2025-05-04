import {useEffect, useRef, useState} from 'react';
import { useLocation, useParams } from 'react-router-dom';
import axios from 'axios';
import './PixelDraw.css';

const PIXEL_SIZE = 15;
const COLORS = ['brown', 'purple']

const PixelDraw = () => {
    const canvasRef = useRef(null);
    const contextRef = useRef(null);

    const location = useLocation();
    const { id } = useParams();

    const [row, setrow] = useState(location.state?.sizeX || 30);
    const [column, setcolumn] = useState(location.state?.sizeY || 30);

    const [grid, setGrid] = useState(Array(row * column).fill('white')); // grid of colored pixels
    const [currentColor, setCurrentColor] = useState('white');
    const [isDrawing, setIsDrawing] = useState(false);

    const [drawingId, setDrawingId] = useState(0);
    const [drawingName, setDrawingName] = useState("");

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
        contextRef.current = ctx;
        drawGrid();
    }, [grid]);

    useEffect(() => {
        setGrid(Array(row * column).fill('white'));
    }, [row, column]);

    useEffect(() =>{
        console.log("urlid: ", id);
        if(id){
            loadDrawing(id);
        }else{
            console.log("no drawing");
        }
    }, []);

    const drawGrid = () =>{
        const ctx = contextRef.current;
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

        // draw pixels
        for(let x = 0; x < column; x++){
            for(let y = 0; y < row; y++){
                ctx.fillStyle = grid[y * column + x];
                ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }    
        }

        // draw grid
        ctx.strokeStyle = 'grey';
        for(let x = 0; x <= row; x++){
            ctx.beginPath();
            ctx.moveTo(0, x * PIXEL_SIZE);
            ctx.lineTo(column * PIXEL_SIZE, x * PIXEL_SIZE);
            ctx.stroke();
        }

        for(let y = 0; y <= column; y++){
            ctx.beginPath();
            ctx.moveTo(y * PIXEL_SIZE, 0);
            ctx.lineTo(y * PIXEL_SIZE, row * PIXEL_SIZE);
            ctx.stroke();
        }
    };

    const paintPixel = (event) => {
        const {offsetX, offsetY} = event.nativeEvent;
        const x = Math.floor(offsetX / PIXEL_SIZE);
        const y = Math.floor(offsetY / PIXEL_SIZE);

        const newGrid = [...grid];
        newGrid[y * row + x] = currentColor;
        setGrid(newGrid);
        drawGrid();
    };

    const handleMouseDown = (event) => {
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
                    grid: JSON.stringify(grid),
                    name: drawingName,
                    size_x: row,
                    size_y: column
                },
                {headers: {"Content-Type": "application/json"}}
            );
            console.log("Drawing saved:", response.data);
        } catch(error){
            console.error('Error while saving drawing:', error);
        }
    }
    
    const loadDrawing = async (id) => {
        if(!id || id <= 0){
            return;
        }
        try {
            const response = await axios.get(`http://localhost:9090/api/drawing/${id}`);
            const drawing = response.data;
            setGrid(JSON.parse(drawing.grid));
            setDrawingName(drawing.name);
            setrow(drawing.size_x);
            setcolumn(drawing.size_y);
            setDrawingId(drawing.id);
            drawGrid();
        } catch(error){
            console.error('Error while loading drawing:', error);
        }
    }

    return(
        <div className="pixel-draw">
            <canvas 
                ref={canvasRef}
                height={column * PIXEL_SIZE}
                width={row * PIXEL_SIZE}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
                style={{ border: "1px solid black" }}
            />
            
            <div>
                {COLORS.map((color) => (
                    <button key={color} onClick={() => changeColor(color)}>
                        {color}
                    </button>
                ))}
            </div>

            <div>
                <div>
                    <input
                            type="text"
                            value={drawingName}
                            onChange={(e) => setDrawingName(e.target.value)}
                            placeholder="Name your drawing"/>
                    <button onClick={saveDrawing}>Save drawing</button>
                </div>
                
                <div>
                    <input 
                        type="number" 
                        value={drawingId}
                        onChange={(e) => setDrawingId(e.target.value)}
                        placeholder="Enter drawing id"/>
                    <button onClick={() => loadDrawing(drawingId)}>Load drawing</button>
                </div>
            </div>
        </div>
    );
}

export default PixelDraw;