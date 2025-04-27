package com.example.backend.controller;

import com.example.backend.dto.AddUserToDrawingDTO;
import com.example.backend.dto.DrawingDTO;
import com.example.backend.dto.UpdateUserDrawingRoleDTO;
import com.example.backend.dto.UserRoleDTO;
import com.example.backend.model.*;
import com.example.backend.service.DrawingService;
import com.example.backend.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
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
    private UserDrawingRole udr;

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

        udr = new UserDrawingRole(
                User.builder().id(1L).name("Ahmed").securityRole(SecurityRole.USER).build(),
                drawing,
                Role.builder().id(1L).build()
        );
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
    void createDrawing_InValidInput_ReturnsBadRequest() throws Exception {
        // precondition
        DrawingDTO dDto = new DrawingDTO(
                "",
                null,
                0,
                0,
                0L
        );

        // action
        ResultActions response = mockMvc.perform(post("/api/drawing", drawing.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dDto)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.grid").exists())
                .andExpect(jsonPath("$.details.name").exists())
                .andExpect(jsonPath("$.details.size_x").exists())
                .andExpect(jsonPath("$.details.size_y").exists())
                .andExpect(jsonPath("$.details.authorId").exists());

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
    void addUserToDrawing_ValidInput_ReturnsCreatedUserDrawingRole() throws Exception{
        // precondition
        Long drawingId = drawing.getId();
        Long roleId = udr.getRole().getId();
        String uName = udr.getUser().getName();

        AddUserToDrawingDTO dto = new AddUserToDrawingDTO(roleId, uName);

        given(drawingService.addUserToDrawing(anyLong(), any(AddUserToDrawingDTO.class)))
                .willReturn(udr);

        // action
        ResultActions response = mockMvc.perform(post("/api/drawing/{id}/user", drawingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // verify
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.drawing.id").value(drawingId))
                .andExpect(jsonPath("$.user.name").value(uName))
                .andExpect(jsonPath("$.role.id").value(roleId));

    }

    @Test
    void addUserToDrawing_InvalidInput_ReturnsBadRequest() throws Exception{
        // precondition
        Long drawingId = drawing.getId();

        AddUserToDrawingDTO dto = new AddUserToDrawingDTO(null, null);

        given(drawingService.addUserToDrawing(anyLong(), any(AddUserToDrawingDTO.class)))
                .willReturn(udr);

        // action
        ResultActions response = mockMvc.perform(post("/api/drawing/{id}/user", drawingId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(dto)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.roleId").exists())
                .andExpect(jsonPath("$.details.name").exists());

    }

    @Test
    void getUsersFromDrawing_IdExists_ReturnUserRoleList() throws Exception{
        // precondition
        Long drawingId = drawing.getId();
        String uName = udr.getUser().getName();

        UserRoleDTO ur = new UserRoleDTO(1L, uName, udr.getRole());
        UserRoleDTO ur2 = new UserRoleDTO(2L, "Jamal", Role.builder().id(2L).build());

        List<UserRoleDTO> urList = List.of(ur, ur2);
        given(drawingService.getDrawingUsers(drawingId)).willReturn(urList);

        // action
        ResultActions response = mockMvc.perform(get("/api/drawing/{id}/user", drawingId));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(urList)));
    }

    @Test
    void getUserFromDrawing_IdExists_ReturnUserRole() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        Long userId = udr.getUser().getId();
        String uName = udr.getUser().getName();

        UserRoleDTO ur = new UserRoleDTO(1L, uName, udr.getRole());
        given(drawingService.getDrawingUser(drawingId, userId)).willReturn(ur);

        // action
        ResultActions response = mockMvc.perform(get("/api/drawing/{id}/user/{userId}", drawingId, userId));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ur)));

    }

    @Test
    void updateUserDrawingRole_ValidInput_ReturnsUpdatedUserDrawingRole() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        Long userId = udr.getUser().getId();
        String uName = udr.getUser().getName();
        Long updateRoleId = 2L;
        String updateRoleName = "new name";

        Role updateRole = Role.builder().id(updateRoleId).name(updateRoleName).build();
        UpdateUserDrawingRoleDTO updateUserDrawingRoleDTO = new UpdateUserDrawingRoleDTO(updateRoleId);
        UserRoleDTO ur = new UserRoleDTO(userId, uName, updateRole);

        given(drawingService.updateUserDrawingRole(userId, drawingId, updateUserDrawingRoleDTO)).willReturn(ur);

        // action
        ResultActions response = mockMvc.perform(put("/api/drawing/{id}/user/{userId}", drawingId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": \"" + updateRoleId.toString() + "\" }"));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ur)));

    }

    @Test
    void updateUserDrawingRole_MissingFields_ReturnsBadRequest() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        Long userId = udr.getUser().getId();

        // action
        ResultActions response = mockMvc.perform(put("/api/drawing/{id}/user/{userId}", drawingId, userId)
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"roleId\": \"null\" }"));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details.roleId").exists());

    }

    @Test
    void deleteUserFromDrawing_ReturnsNoContent() throws Exception {
        // precondition
        Long drawingId = drawing.getId();
        Long userId = udr.getUser().getId();

        willDoNothing().given(drawingService).deleteUserDrawingRole(userId, drawingId);

        // action
        ResultActions response = mockMvc.perform(delete("/api/drawing/{id}/user/{userId}", drawingId, userId));

        // verify
        response.andExpect(status().isNoContent());
    }
}