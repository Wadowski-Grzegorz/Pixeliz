package com.example.backend.controller;

import com.example.backend.model.Role;
import com.example.backend.service.RoleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = RoleController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
class RoleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private RoleService roleService;

    private Role role;

    @BeforeEach
    void setUp() {
        role = Role
                .builder()
                .id(1L)
                .name("admin")
                .admin(true)
                .read(true)
                .write(true)
                .delete(true)
                .build();
    }

    @Test
    void getAllRoles_ReturnsRoles() throws Exception {
        // precondition
        Role role2 = Role.builder().id(2L).build();
        List<Role> roles = List.of(role, role2);
        given(roleService.getRoles()).willReturn(roles);

        // action
        ResultActions response = mockMvc.perform(get("/api/role"));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roles)));
    }

    @Test
    void getRole_IdExists_ReturnsRole() throws Exception {
        // precondition
        given(roleService.getRole(role.getId())).willReturn(role);

        // action
        ResultActions response = mockMvc.perform(get("/api/role/{id}", role.getId()));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(role)));
    }
}