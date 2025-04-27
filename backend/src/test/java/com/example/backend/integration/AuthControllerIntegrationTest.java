package com.example.backend.integration;


import com.example.backend.dto.LoginDTO;
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


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerIntegrationTest {
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
    void login_ValidInput_ReturnsToken() throws Exception {
        // precondition
        LoginDTO loginDTO = LoginDTO
                .builder()
                .email("jamal@email.com")
                .password("StrongPassword")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // verify
        response.andExpect(status().isOk())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void login_MissingLoginAndEmail_ReturnsBadRequest() throws Exception {
        // precondition
        LoginDTO loginDTO = LoginDTO
                .builder()
                .password("StrongPassword")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void login_MissingPassword_ReturnsBadRequest() throws Exception {
        // precondition
        LoginDTO loginDTO = LoginDTO
                .builder()
                .email("jamal@email.com")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }

    @Test
    void login_InvalidInput_ReturnsNotFound() throws Exception {
        // precondition
        LoginDTO loginDTO = LoginDTO
                .builder()
                .email("hackerman@mail.com")
                .password("hackerman")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginDTO)));

        // verify
        response.andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details").isNotEmpty());
    }


    @Test
    void register_ValidInput_ReturnsToken() throws Exception {
        // precondition
        UserDTO userDTO = UserDTO
                .builder()
                .name("Ahmed")
                .login("Ahmed445")
                .password("AhmedsPassword")
                .email("ahmed@email.com")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        // verify
        response.andExpect(status().isCreated())
                .andExpect(jsonPath("$.token").isNotEmpty());
    }

    @Test
    void register_MissingInput_ReturnsBadRequest() throws Exception {
        // precondition
        UserDTO userDTO = UserDTO
                .builder()
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        // verify
        response.andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.details").isNotEmpty());
    }

    @Test
    void register_DuplicateUser_ReturnsConflict() throws Exception {
        // precondition
        UserDTO userDTO = UserDTO
                .builder()
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .build();

        // action
        ResultActions response = mockMvc.perform(post("/api/auth/register")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(userDTO)));

        // verify
        response.andExpect(status().isConflict())
                .andExpect(jsonPath("$.error").isNotEmpty());
    }
}
