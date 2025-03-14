package com.example.backend.controller;

import com.example.backend.model.Drawing;
import com.example.backend.service.DrawingService;
import com.example.backend.dto.DrawingRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class DrawingController {

    private final DrawingService drawingService;

    public DrawingController(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @PostMapping("/createDrawing")
    public ResponseEntity<Drawing> createDrawing(@RequestBody DrawingRequest drawingRequest) {
        Drawing savedDrawing = drawingService.createDrawing(drawingRequest);
        return ResponseEntity.ok(savedDrawing);
    }

    @GetMapping("/getDrawing/{id}")
    public ResponseEntity<Drawing> getDrawing(@PathVariable Long id) {
        Optional<Drawing> drawing = drawingService.getDrawing(id);
        return drawing.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
