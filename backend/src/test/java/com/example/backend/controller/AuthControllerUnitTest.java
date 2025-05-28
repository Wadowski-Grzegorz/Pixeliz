package com.example.backend.controller;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.LoginDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {AuthController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class AuthControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    @Test
    void register_ValidInput_ReturnsToken() throws Exception{
        // precondition
        UserDTO uDto = UserDTO
                .builder()
                .name("Jamal")
                .login("Jamalito")
                .email("jamal@mail.com")
                .password("jamalpass")
                .build();
        AuthResponseDTO authDto = new AuthResponseDTO("I am a token");
        given(userService.createUser(any(UserDTO.class))).willReturn(authDto);

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uDto)));

        // verify
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").exists());
    }

    @Test
    void register_MissingFields_ReturnsBadRequest() throws Exception{
        // precondition
        UserDTO uDto = UserDTO
                .builder()
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uDto)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.login").exists())
                .andExpect(jsonPath("$.details.password").exists())
                .andExpect(jsonPath("$.details.email").exists());
    }

    @Test
    void login_validInput_ReturnsToken() throws Exception {
        // precondition
        LoginDTO loginData = LoginDTO
                .builder()
                .email("jamal@mail.com")
                .password("jamalpass")
                .build();
        AuthResponseDTO authDto = new AuthResponseDTO("I am a token");
        given(userService.login(any(LoginDTO.class))).willReturn(authDto);

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginData)));

        // verify
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").exists());

    }

    @Test
    void login_MissingFields_ReturnsBadRequest() throws Exception {
        // precondition
        LoginDTO loginData = LoginDTO
                .builder()
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginData)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.password").exists());
    }

    @Test
    void logout() {
    }
}