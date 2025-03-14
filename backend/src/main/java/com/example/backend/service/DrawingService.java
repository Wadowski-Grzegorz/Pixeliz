package com.example.backend.service;

import com.example.backend.dto.DrawingRequest;
import com.example.backend.model.Drawing;
import com.example.backend.repository.DrawingRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Drawing savedDrawing = drawingRepository.save(drawing);

        return savedDrawing;
    }

    public Optional<Drawing> getDrawing(Long id) {
        return drawingRepository.findById(id);
    }
}
