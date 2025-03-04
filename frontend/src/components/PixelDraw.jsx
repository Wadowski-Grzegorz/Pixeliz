import {useEffect, useRef, useState} from 'react';
import axios from 'axios';
import './PixelDraw.css';

const ROW = 30;
const COLUMN = 30;
const PIXEL_SIZE = 15;
const COLORS = ['brown', 'purple']

const PixelDraw = () => {
    const canvasRef = useRef(null);
    const contextRef = useRef(null);

    const [grid, setGrid] = useState(Array(ROW * COLUMN).fill('white')); // grid of colored pixels
    const [currentColor, setCurrentColor] = useState('white');
    const [isDrawing, setIsDrawing] = useState(false);

    useEffect(() => {
        const canvas = canvasRef.current;
        const ctx = canvas.getContext("2d");
        contextRef.current = ctx;
        drawGrid();
    }, [grid]);

    const drawGrid = () =>{
        const ctx = contextRef.current;
        ctx.clearRect(0, 0, ctx.canvas.width, ctx.canvas.height);

        // draw pixels
        for(let x = 0; x < COLUMN; x++){
            for(let y = 0; y < ROW; y++){
                ctx.fillStyle = grid[x * ROW + y];
                ctx.fillRect(x * PIXEL_SIZE, y * PIXEL_SIZE, PIXEL_SIZE, PIXEL_SIZE);
            }    
        }

        // draw grid
        ctx.strokeStyle = 'grey';
        for(let x = 0; x <= COLUMN; x++){
            ctx.beginPath();
            ctx.moveTo(0, x * PIXEL_SIZE);
            ctx.lineTo(COLUMN * PIXEL_SIZE, x * PIXEL_SIZE);
            ctx.stroke();
        }

        for(let y = 0; y <= ROW; y++){
            ctx.beginPath();
            ctx.moveTo(y * PIXEL_SIZE, 0);
            ctx.lineTo(y * PIXEL_SIZE, ROW * PIXEL_SIZE);
            ctx.stroke();
        }
    };

    const paintPixel = (event) => {
        const {offsetX, offsetY} = event.nativeEvent;
        const x = Math.floor(offsetX / PIXEL_SIZE);
        const y = Math.floor(offsetY / PIXEL_SIZE);

        const newGrid = [...grid];
        newGrid[x * ROW + y] = currentColor;
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
            const response = await axios.post('http://localhost:8080/api/saveDrawing', 
                {grid: JSON.stringify(grid)},
                {headers: {"Content-Type": "application/json"}}
            );
            console.log("Drawing saved:", response.data);
        } catch(error){
            console.error('Error while saving drawing:', error);
        }
    }
    
    const loadDrawing = async (id) => {
        if(!id){
            return;
        }
        try {
            const response = await axios.get(`http://localhost:8080/api/getDrawing/${id}`);
            const loadedGrid = JSON.parse(response.data.gridData);
            setGrid(loadedGrid);
            drawGrid();
        } catch(error){
            console.error('Error while loading drawing:', error);
        }
    }

    return(
        <>
            <canvas 
                ref={canvasRef}
                width={COLUMN * PIXEL_SIZE}
                height={ROW * PIXEL_SIZE}
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
                <button onClick={saveDrawing}>Save drawing</button>
                <button onClick={() => loadDrawing(1)}>Load drawing</button>
            </div>
        </>
    );
}

export default PixelDraw;