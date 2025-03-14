package com.example.backend.controller;

import com.example.backend.dto.DrawingUpdateRequest;
import com.example.backend.model.Drawing;
import com.example.backend.service.DrawingService;
import com.example.backend.dto.DrawingRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/drawing")
@CrossOrigin(origins = "http://localhost:5173")
public class DrawingController {

    private final DrawingService drawingService;

    public DrawingController(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @PostMapping()
    public ResponseEntity<Drawing> createDrawing(@RequestBody DrawingRequest drawingRequest) {
        Drawing drawing = drawingService.createDrawing(drawingRequest);
        return new ResponseEntity<>(drawing, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drawing> getDrawing(@PathVariable Long id) {
        try{
            Drawing drawing = drawingService.getDrawing(id);
            return new ResponseEntity<>(drawing, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public ResponseEntity<List<Drawing>> getDrawings() {
        return new ResponseEntity<>(drawingService.getDrawings(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drawing> putDrawing(@PathVariable Long id, @RequestBody DrawingUpdateRequest drawingUpdateRequest) {
        try{
            Drawing drawing = drawingService.updateDrawing(id, drawingUpdateRequest);
            return new ResponseEntity<>(drawing, HttpStatus.OK);
        } catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrawing(@PathVariable Long id){
        try{
            drawingService.deleteDrawing(id);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }catch(IllegalArgumentException e){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
