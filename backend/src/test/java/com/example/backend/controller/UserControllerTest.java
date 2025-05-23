package com.example.backend.controller;

import com.example.backend.dto.AuthResponseDTO;
import com.example.backend.dto.UserDTO;
import com.example.backend.model.SecurityRole;
import com.example.backend.model.User;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = {UserController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserService userService;

    @MockitoBean
    private JwtService jwtService;

    private User user;

    @BeforeEach
    void setUp() {
        user = User
                .builder()
                .id(1L)
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .securityRole(SecurityRole.USER)
                .build();

        var auth = new UsernamePasswordAuthenticationToken(
                "username",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getUser_IdExists() throws Exception {
        // precondition
        given(userService.getUser(user.getId())).willReturn(user);

        // action
        ResultActions response = mockMvc.perform(get("/api/user/{id}", user.getId()));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

    }

    @Test
    void getUsers_ReturnsUsers() throws Exception {
        // precondition
        User user2 = User.builder().id(2L).securityRole(SecurityRole.USER).build();
        List<User> users = List.of(user, user2);
        given(userService.getUsers()).willReturn(users);

        // action
        ResultActions response = mockMvc.perform(get("/api/user"));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void updateUser_IdExists_ReturnsUpdatedUser() throws Exception {
        // precondition
        UserDTO uDto = UserDTO
                .builder()
                .name("newName")
                .login("newLogin")
                .email("newEmail")
                .password("newPassword")
                .build();

        String token = "I am a token";
        AuthResponseDTO authResponseDTO = new AuthResponseDTO(token);
        given(userService.updateUser(
                anyLong(), any(UserDTO.class), anyString(), anyBoolean()))
                .willReturn(authResponseDTO);

        // action
        ResultActions response = mockMvc.perform(put("/api/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uDto)));

        // verify
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value(token));
    }

    @Test
    void deleteUser_IdExists_ReturnsNoContent() throws Exception {
        // precondition
        willDoNothing().given(userService).deleteUser(anyLong(), anyString(), anyBoolean());

        // action
        ResultActions response = mockMvc.perform(delete("/api/user/{id}", user.getId()));

        // verify
        response.andExpect(status().isNoContent());
    }
}