package com.example.backend.integration;

import com.example.backend.model.Role;
import com.example.backend.repository.RoleRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class RoleControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Role role;
    @Autowired
    private RoleRepository roleRepository;

    @BeforeEach
    void setUp() {
        roleRepository.deleteAll();
        role = Role
                .builder()
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
        roleRepository.save(role);
    }

    @Test
    void getAllRoles_ReturnsRoles() throws Exception {
        // given
        Role role2 = Role.builder()
                .name("moderator")
                .admin(true)
                .read(true)
                .write(true)
                .delete(false)
                .build();
        roleRepository.save(role2);
        List<Role> roles = List.of(role, role2);

        // when
        ResultActions response = mockMvc.perform(get("/api/role"));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }

    @Test
    void getRole_IdExists_ReturnsRole() throws Exception {
        // given

        // when
        ResultActions response = mockMvc.perform(get("/api/role/{id}", role.getId()));

        // then
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(role)));
    }
}