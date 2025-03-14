package com.example.backend.service;

import com.example.backend.dto.DrawingRequest;
import com.example.backend.dto.DrawingUpdateRequest;
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

    public Drawing createDrawing(DrawingRequest drawingRequest) {
        Drawing drawing = new Drawing();
        drawing.setName(drawingRequest.getName());
        drawing.setGrid(drawingRequest.getGrid());
        drawing.setSize_x(drawingRequest.getSize_x());
        drawing.setSize_y(drawingRequest.getSize_y());

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

    public Drawing updateDrawing(Long id, DrawingUpdateRequest drawingUpdateRequest) {
        Drawing drawing = getDrawing(id);
        drawing.setName(drawingUpdateRequest.getName());
        drawing.setGrid(drawingUpdateRequest.getGrid());
        return drawingRepository.save(drawing);
    }

    public void deleteDrawing(Long id){
        getDrawing(id);
        drawingRepository.deleteById(id);
    }
}
