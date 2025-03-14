package com.example.backend.service;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.model.Drawing;
import com.example.backend.repository.DrawingRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class DrawingService {

    private final DrawingRepository drawingRepository;

    public DrawingService(DrawingRepository drawingRepository) {
        this.drawingRepository = drawingRepository;
    }

    public Drawing createDrawing(DrawingDTO drawingDTO) {
        Drawing drawing = new Drawing();
        drawing.setName(drawingDTO.getName());
        drawing.setGrid(drawingDTO.getGrid());
        drawing.setSize_x(drawingDTO.getSize_x());
        drawing.setSize_y(drawingDTO.getSize_y());

        return drawingRepository.save(drawing);
    }

    public Drawing getDrawing(Long id) {
        return drawingRepository
                .findById(id)
                .orElseThrow(() -> new NoSuchElementException("Drawing not found"));
    }

    public List<Drawing> getDrawings(){
        return drawingRepository.findAll();
    }

    public Drawing updateDrawing(Long id, DrawingDTO drawingUpdate) {
        Drawing drawing = getDrawing(id);
        drawing.setName(drawingUpdate.getName());
        drawing.setGrid(drawingUpdate.getGrid());
        return drawingRepository.save(drawing);
    }

    public void deleteDrawing(Long id){
        getDrawing(id);
        drawingRepository.deleteById(id);
    }
}
