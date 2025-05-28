package com.example.backend.controller;

import com.example.backend.model.UserStatistics;
import com.example.backend.service.JwtService;
import com.example.backend.service.UserStatisticsService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = {UserStatisticsController.class})
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserStatisticsControllerUnitTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private UserStatisticsService userStatisticsService;

    @MockitoBean
    private JwtService jwtService;

    private UserStatistics userStatistics;

    @BeforeEach
    void setUp() {
        userStatistics = UserStatistics
                .builder()
                .id(1L)
                .build();

        var auth = new UsernamePasswordAuthenticationToken(
                "username",
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getUserStatisticsByUserId_IdDoesExists_ReturnsUserStatistics() throws Exception {
        // precondition
        Long id = 1L;
        given(userStatisticsService.getUserStatisticsByUserId(eq(id))).willReturn(userStatistics);

        // action
        ResultActions response = mockMvc.perform(get("/api/user/stats/{userId}", id));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userStatistics)));
    }

    @Test
    void clickUserStatistics_IdDoesExists_ReturnsUserStatistics() throws Exception {
        // precondition
        String username = "username";

        // action
        ResultActions response = mockMvc.perform(post("/api/user/stats"));

        // verify
        verify(userStatisticsService, times(1))
                .clickToQueue(eq(username));
        response.andExpect(status().isOk());
    }
}
