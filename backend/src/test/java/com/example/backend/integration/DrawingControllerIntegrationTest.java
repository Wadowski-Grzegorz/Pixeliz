package com.example.backend.integration;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.model.Drawing;
import com.example.backend.repository.DrawingRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class DrawingControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private DrawingRepository drawingRepository;

    private Drawing drawing;

    @BeforeEach
    void setUp() {
        drawingRepository.deleteAll();
        drawing = Drawing
                .builder()
                .grid("[\"white\",\"white\"]")
                .name("my drawing")
                .size_x(1)
                .size_y(2)
                .build();
        drawingRepository.save(drawing);
    }

    @Test
    void createDrawing_ValidInput_ReturnsCreatedDrawing() throws Exception {
        // given
        DrawingDTO dDto = DrawingDTO
                .builder()
                .grid("[\"white\",\"white\"]")
                .name("my new drawing")
                .size_x(1)
                .size_y(2)
                .build();

        // when
        ResultActions response = mockMvc.perform(post("/api/drawing")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dDto)));

        // then
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("my new drawing"))
                .andExpect(jsonPath("$.size_x").value(1))
                .andExpect(jsonPath("$.size_y").value(2));
    }


    @Test
    void getDrawing_IdExists_ReturnsDrawing() throws Exception {
        // given

        // when
        ResultActions response = mockMvc.perform(get("/api/drawing/{id}", drawing.getId()));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(drawing)));

    }

    @Test
    void getDrawings_ReturnsDrawings() throws Exception {
        // precondition
        Drawing drawing2 = Drawing
                .builder()
                .grid("[\"red\",\"red\"]")
                .name("second drawing")
                .size_x(1)
                .size_y(2)
                .build();
        drawing2 = drawingRepository.save(drawing2);
        List<Drawing> drawings = List.of(drawing, drawing2);

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
                .id(drawingId)
                .grid(dDto.getGrid())
                .name(dDto.getName())
                .size_x(drawing.getSize_x())
                .size_y(drawing.getSize_y())
                .build();

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

        // action
        ResultActions response = mockMvc.perform(delete("/api/drawing/{id}", drawingId));

        // verify
        response.andExpect(status().isNoContent());
    }

}