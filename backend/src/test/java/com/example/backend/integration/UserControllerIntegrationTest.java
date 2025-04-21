package com.example.backend.integration;


import com.example.backend.dto.UserDTO;
import com.example.backend.model.SecurityRole;
import com.example.backend.model.User;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    private User user;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        user = User
                .builder()
                .name("Jamal")
                .login("Jamal445")
                .password(passwordEncoder.encode("StrongPassword"))
                .email("jamal@email.com")
                .securityRole(SecurityRole.USER)
                .build();
        userRepository.save(user);
    }

    @Test
    void getUser_IdExists() throws Exception {
        // precondition

        // action
        ResultActions response = mockMvc.perform(get("/api/user/{id}", user.getId()));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));

    }

    @Test
    void getUsers_ReturnsUsers() throws Exception {
        // precondition
        User user2 = User.builder()
                .email("jamal2@email.com")
                .name("Jamal2")
                .password(passwordEncoder.encode("StrongPassword2"))
                .login("jama877654")
                .securityRole(SecurityRole.USER)
                .build();
        user2 = userRepository.save(user2);
        List<User> users = List.of(user, user2);

        // action
        ResultActions response = mockMvc.perform(get("/api/user"));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(users)));
    }

    @Test
    void updateUser_IdExists_ReturnsUpdatedUser() throws Exception {
        // precondition
        UserDTO uDto = new UserDTO(
                "newName",
                "newLogin",
                "newEmail@email.com",
                "newPassword"
        );

        // action
        ResultActions response = mockMvc.perform(put("/api/user/{id}", user.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(uDto)));

        // verify
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(user.getId()))
                .andExpect(jsonPath("$.name").value(uDto.getName()))
                .andExpect(jsonPath("$.login").value(uDto.getLogin()))
                .andExpect(jsonPath("$.email").value(uDto.getEmail()));

        String json = response.andReturn().getResponse().getContentAsString();
        String returnedPassword = objectMapper.readTree(json).get("password").asText();
        assertTrue(passwordEncoder.matches(uDto.getPassword(), returnedPassword));
    }

    @Test
    void deleteUser_IdExists() throws Exception {
        // precondition

        // action
        ResultActions response = mockMvc.perform(delete("/api/user/{id}", user.getId()));

        // verify
        response.andExpect(status().isNoContent());
    }
}