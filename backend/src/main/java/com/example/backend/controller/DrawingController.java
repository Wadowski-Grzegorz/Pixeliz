package com.example.backend.controller;

import com.example.backend.dto.AddUserToDrawingDTO;
import com.example.backend.dto.UserRoleDTO;
import com.example.backend.model.Drawing;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.service.DrawingService;
import com.example.backend.dto.DrawingDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/drawing")
@CrossOrigin(origins = "http://localhost:5173")
public class DrawingController {

    private final DrawingService drawingService;

    public DrawingController(DrawingService drawingService) {
        this.drawingService = drawingService;
    }

    @PostMapping()
    public ResponseEntity<Drawing> createDrawing(@RequestBody @Valid DrawingDTO drawingDTO) {
        Drawing drawing = drawingService.createDrawing(drawingDTO);
        return new ResponseEntity<>(drawing, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Drawing> getDrawing(@PathVariable Long id) {
        Drawing drawing = drawingService.getDrawing(id);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @GetMapping()
    public ResponseEntity<List<Drawing>> getDrawings() {
        return new ResponseEntity<>(drawingService.getDrawings(), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Drawing> putDrawing(
            @PathVariable Long id,
            @RequestBody @Validated(DrawingDTO.Update.class) DrawingDTO drawingUpdate) {
        Drawing drawing = drawingService.updateDrawing(id, drawingUpdate);
        return new ResponseEntity<>(drawing, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDrawing(@PathVariable Long id){
        drawingService.deleteDrawing(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }



    @PostMapping("/{id}/user")
    public ResponseEntity<?> addUserToDrawing(@PathVariable Long id,
                                              @RequestBody @Valid AddUserToDrawingDTO AddUserToDrawingDTO){
        Long roleId = AddUserToDrawingDTO.getRoleId();
        String username = AddUserToDrawingDTO.getUsername();
        UserDrawingRole relation = drawingService.addUserToDrawing(id, roleId, username);
        return new ResponseEntity<>(relation, HttpStatus.CREATED);
    }

    @GetMapping("/{id}/user")
    public ResponseEntity<List<UserRoleDTO>> getUsersFromDrawing(@PathVariable Long id){
        return new ResponseEntity<>(drawingService.getDrawingUsers(id), HttpStatus.OK);
    }

    @GetMapping("/{id}/user/{userId}")
    public ResponseEntity<UserRoleDTO> getUserFromDrawing(@PathVariable Long id, @PathVariable Long userId){
        return new ResponseEntity<>(drawingService.getDrawingUser(id, userId), HttpStatus.OK);
    }

    @PutMapping("{id}/user/{userId}")
    public ResponseEntity<?> updateUserRole(@PathVariable Long id,
                                            @PathVariable Long userId,
                                            @RequestBody Map<String, Long> requestBody){
        Long roleId = requestBody.get("roleId");
        return new ResponseEntity<>(drawingService.updateUserDrawingRole(roleId, userId, id), HttpStatus.OK);
    }

    @DeleteMapping("/{id}/user/{userId}")
    public ResponseEntity<?> deleteUserFromDrawing(@PathVariable Long id, @PathVariable Long userId){
        drawingService.deleteUserDrawingRole(id, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }


}
