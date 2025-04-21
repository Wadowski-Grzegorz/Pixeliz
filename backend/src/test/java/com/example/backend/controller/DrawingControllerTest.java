package com.example.backend.controller;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.Drawing;
import com.example.backend.model.User;
import com.example.backend.service.DrawingService;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {DrawingController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class DrawingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private DrawingService drawingService;

    @MockitoBean
    private JwtService jwtService;

    private Drawing drawing;

    @BeforeEach
    void setUp() {
        drawing = Drawing
                .builder()
                .id(1L)
                .grid("[\"white\",\"white\"]")
                .name("my drawing")
                .size_x(1)
                .size_y(2)
                .build();
    }

    @Test
    void createDrawing_ValidInput_ReturnsCreatedDrawing() throws Exception {
        // precondition
        DrawingDTO dDto = new DrawingDTO(
                "[\"white\",\"white\"]",
                "my drawing",
                1,
                2,
                1L
        );
        given(drawingService.createDrawing(any(DrawingDTO.class))).willReturn(drawing);

        // action
        ResultActions response = mockMvc.perform(post("/api/drawing", drawing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dDto)));

        // verify
        response.andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(drawing)));

    }

    @Test
    void getDrawing_IdExists_ReturnsDrawing() throws Exception {
        // precondition
        given(drawingService.getDrawing(drawing.getId())).willReturn(drawing);

        // action
        ResultActions response = mockMvc.perform(get("/api/drawing/{id}", drawing.getId()));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(drawing)));

    }

    @Test
    void getDrawings_ReturnsDrawings() throws Exception {
        // precondition
        Drawing drawing2 = Drawing.builder().id(2L).grid("[\"red\",\"red\"]").name("second drawing").size_x(1).size_y(2).build();
        List<Drawing> drawings = List.of(drawing, drawing2);
        given(drawingService.getDrawings()).willReturn(drawings);

        // action
        ResultActions response = mockMvc.perform(get("/api/drawing"));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(drawings)));
    }

    @Test
    void putDrawing() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        DrawingDTO dDto = DrawingDTO
                .builder()
                .grid("[\"brown\",\"brown\"]")
                .name("my updated drawing")
                .build();
        Drawing updatedDrawing = Drawing.builder()
                .grid(dDto.getGrid())
                .name(dDto.getName())
                .build();

        given(drawingService.updateDrawing(eq(drawingId), any(DrawingDTO.class))).willReturn(updatedDrawing);

        // action
        ResultActions response = mockMvc.perform(put("/api/drawing/{id}", drawingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dDto)));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(updatedDrawing)));
    }

    @Test
    void deleteDrawing() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        willDoNothing().given(drawingService).deleteDrawing(drawingId);

        // action
        ResultActions response = mockMvc.perform(delete("/api/drawing/{id}", drawingId));

        // verify
        verify(drawingService, times(1)).deleteDrawing(drawingId);
        response.andExpect(status().isNoContent());
    }


    @Test
    void addUserToDrawing() {
    }

    @Test
    void getUsersFromDrawing() {
    }

    @Test
    void getUserFromDrawing() {
    }

    @Test
    void updateUserRole() {
    }

    @Test
    void deleteUserFromDrawing() {
    }
}