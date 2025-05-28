package com.example.backend.integration;

import com.example.backend.model.SecurityRole;
import com.example.backend.model.User;
import com.example.backend.model.UserStatistics;
import com.example.backend.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.testcontainers.containers.RabbitMQContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Testcontainers
public class UserStatisticsControllerIntegrationTest {

    @Container
    public static RabbitMQContainer rabbitMQContainer =
            new RabbitMQContainer("rabbitmq:3.10.7");

    @DynamicPropertySource
    static void configureProperties(org.springframework.test.context.DynamicPropertyRegistry registry) {
        registry.add("spring.rabbitmq.host", rabbitMQContainer::getHost);
        registry.add("spring.rabbitmq.port", rabbitMQContainer::getAmqpPort);
    }

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;


    private UserStatistics userStatistics;
    private User user;

    @BeforeEach
    void setUp(){
        userRepository.deleteAll();
        userStatistics = new UserStatistics();
        user = User
                .builder()
                .name("Jamal")
                .login("Jamal445")
                .password(passwordEncoder.encode("StrongPassword"))
                .email("jamal@email.com")
                .userStatistics(userStatistics)
                .securityRole(SecurityRole.USER)
                .build();
        userRepository.save(user);

        var auth = new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                null,
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
        SecurityContextHolder.getContext().setAuthentication(auth);
    }

    @Test
    void getUserStatisticsByUserId_IdDoesExists_ReturnsUserStatistics() throws Exception {
        // precondition
        Long id = 1L;

        // action
        ResultActions response = mockMvc.perform(get("/api/user/stats/{userId}", id));

        // verify
        response.andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(userStatistics)));
    }

    @Test
    void clickUserStatistics_IdDoesExists_ReturnsUserStatistics() throws Exception {
        // precondition

        // action
        ResultActions response = mockMvc.perform(post("/api/user/stats"));

        // verify
        response.andExpect(status().isOk());
    }
}
