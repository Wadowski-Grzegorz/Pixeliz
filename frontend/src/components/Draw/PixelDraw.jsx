import {useEffect, useRef, useState} from 'react';
import { useLocation, useParams } from 'react-router-dom';
import axios from 'axios';
import './PixelDraw.css';

const PIXEL_SIZE = 15;
const COLORS = ['brown', 'purple']

const PixelDraw = () => {
    const contextRef = useRef(null);
    const canvasRef = useRef(null);
    const gridRef = useRef(null);

    const location = useLocation();
    const { id } = useParams();

    const [row, setRow] = useState(location.state?.sizeX || 30);
    const [column, setColumn] = useState(location.state?.sizeY || 30);

    const [grid, setGrid] = useState(Array(row * column).fill('white')); // grid of colored pixels
    const [currentColor, setCurrentColor] = useState('white');
    const [isDrawing, setIsDrawing] = useState(false);

    const [drawingId, setDrawingId] = useState(0);
    const [drawingName, setDrawingName] = useState("");

    const [redraw, setRedraw] = useState(false);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
        contextRef.current = ctx;
        
        drawSheet();
        drawGrid();
        setRedraw(false);
    }, [redraw]);

    useEffect(() =>{
        console.log("urlid: ", id);
        if(id){
            loadDrawing(id);
        }else{
            console.log("no drawing");
        }
    }, []);

    const drawSheet = () =>{
        const ctx = contextRef.current;
        for(let x = 0; x < row; x++){
            for(let y = 0; y < column; y++){
                ctx.fillStyle = grid[y * row + x];
                ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }    
        }
    }

    const drawGrid = () =>{
        const ctx = gridRef.current.getContext("2d");
        

        // draw grid
        ctx.strokeStyle = 'grey';
        
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

        const newGrid = [...grid];
        const index = y * row + x;
        
        if(grid[index] === currentColor){
            return;
        }

        newGrid[index] = currentColor;
        ctx.fillStyle = currentColor;

        ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);

        setGrid(newGrid);
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
            setRow(drawing.size_x);
            setColumn(drawing.size_y);
            setDrawingId(drawing.id);
            setRedraw(true);
        } catch(error){
            console.error('Error while loading drawing:', error);
        }
    }

    return(
        <div className="pixel-draw" style={{position: 'relative'}}>
            <canvas 
                ref={canvasRef}
                height={column * PIXEL_SIZE}
                width={row * PIXEL_SIZE}
                onMouseDown={handleMouseDown}
                onMouseMove={handleMouseMove}
                onMouseUp={handleMouseUp}
                onMouseLeave={handleMouseUp}
                style={{ 
                    position: 'relative',
                    top: 0,
                    left: 0,
                    zIndex: 0
                 }}
            />

            <canvas 
                ref={gridRef}
                height={column * PIXEL_SIZE}
                width={row * PIXEL_SIZE}
                style={{ 
                    border: "1px solid black",
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    zIndex: 1,
                    pointerEvents: 'none'
                }}
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