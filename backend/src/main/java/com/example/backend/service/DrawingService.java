package com.example.backend.service;

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

    public Drawing saveDrawing(String gridData) {
        return drawingRepository.save(new Drawing(null, gridData));
    }

    public Optional<Drawing> getDrawing(Long id) {
        return drawingRepository.findById(id);
    }
}
