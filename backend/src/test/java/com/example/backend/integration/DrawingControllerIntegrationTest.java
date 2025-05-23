package com.example.backend.integration;

import com.example.backend.dto.DrawingDTO;
import com.example.backend.model.Drawing;
import com.example.backend.model.Role;
import com.example.backend.model.User;
import com.example.backend.model.UserDrawingRole;
import com.example.backend.repository.DrawingRepository;
import com.example.backend.repository.RoleRepository;
import com.example.backend.repository.UserDrawingRoleRepository;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserDrawingRoleRepository userDrawingRoleRepository;

    private Drawing drawing;

    @BeforeEach
    void setUp() {
        userDrawingRoleRepository.deleteAll();

        drawingRepository.deleteAll();
        drawing = Drawing
                .builder()
                .pixels(List.of("#FFFFFF", "#FFFFFF"))
                .name("my drawing")
                .size_x(1)
                .size_y(2)
                .build();
        drawingRepository.save(drawing);

        userRepository.deleteAll();
        User user = User
                .builder()
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .build();
        userRepository.save(user);

        roleRepository.deleteAll();
        Role role = Role
                .builder()
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
        roleRepository.save(role);

        UserDrawingRole udr = new UserDrawingRole(user, drawing, role);
        userDrawingRoleRepository.save(udr);

        var auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    // h2 doesn't read jsonb
//    @Test
//    void createDrawing_ValidInput_ReturnsCreatedDrawing() throws Exception {
//        // given
//        DrawingDTO dDto = DrawingDTO
//                .builder()
//                .pixels(List.of("#FFFFFF", "#FFFFFF"))
//                .name("my new drawing")
//                .size_x(1)
//                .size_y(2)
//                .build();
//
//        // when
//        ResultActions response = mockMvc.perform(post("/api/drawing")
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dDto)));
//
//        // then
//        response.andExpect(status().isCreated())
//                .andExpect(jsonPath("$.id").isNotEmpty())
//                .andExpect(jsonPath("$.name").value("my new drawing"))
//                .andExpect(jsonPath("$.size_x").value(1))
//                .andExpect(jsonPath("$.size_y").value(2));
//    }


//    @Test
//    void getDrawing_IdExists_ReturnsDrawing() throws Exception {
//        // given
//
//        // when
//        ResultActions response = mockMvc.perform(get("/api/drawing/{id}", drawing.getId()));
//
//        // then
//        response.andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(drawing)));
//
//    }

//    @Test
//    void getDrawings_ReturnsDrawings() throws Exception {
//        // precondition
//        Drawing drawing2 = Drawing
//                .builder()
//                .pixels(List.of("#FFFFFF", "#FFFFFF"))
//                .name("second drawing")
//                .size_x(1)
//                .size_y(2)
//                .build();
//        drawing2 = drawingRepository.save(drawing2);
//        List<Drawing> drawings = List.of(drawing, drawing2);
//
//        // action
//        ResultActions response = mockMvc.perform(get("/api/drawing"));
//
//        // verify
//        response.andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(drawings)));
//    }

//    @Test
//    void putDrawing() throws Exception {
//        // precondition
//        Long drawingId = drawing.getId();
//        DrawingDTO dDto = DrawingDTO
//                .builder()
//                .pixels(drawing.getPixels())
//                .name("my updated drawing")
//                .build();
//        Drawing updatedDrawing = Drawing.builder()
//                .id(drawingId)
//                .pixels(dDto.getPixels())
//                .name(dDto.getName())
//                .size_x(drawing.getSize_x())
//                .size_y(drawing.getSize_y())
//                .build();
//
//        // action
//        ResultActions response = mockMvc.perform(put("/api/drawing/{id}", drawingId)
//                .contentType(MediaType.APPLICATION_JSON)
//                .content(objectMapper.writeValueAsString(dDto)));
//
//        // verify
//        response.andExpect(status().isOk())
//                .andExpect(content().json(objectMapper.writeValueAsString(updatedDrawing)));
//    }

//    @Test
//    void deleteDrawing() throws Exception {
//        // precondition
//        Long drawingId = drawing.getId();
//
//        // action
//        ResultActions response = mockMvc.perform(delete("/api/drawing/{id}", drawingId));
//
//        // verify
//        response.andExpect(status().isNoContent());
//    }

}