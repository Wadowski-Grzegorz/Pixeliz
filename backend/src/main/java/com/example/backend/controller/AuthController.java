package com.example.backend.controller;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.User;
import com.example.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
@RestController
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponseDTO> register(@RequestBody @Valid UserDTO userDTO){
        AuthResponseDTO response = userService.createUser(userDTO);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponseDTO> login(@RequestBody @Valid LoginDTO loginDTO){
        AuthResponseDTO response = userService.login(loginDTO);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("logout")
    public ResponseEntity<?> logout(){
        return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
    }


}
