package com.example.backend.service;

import com.example.backend.config.RabbitMQConfig;
import com.example.backend.model.User;
import com.example.backend.model.UserStatistics;
import com.example.backend.repository.UserStatisticsRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.amqp.rabbit.core.RabbitTemplate;


import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.eq;

@ExtendWith(MockitoExtension.class)
public class UserStatisticsServiceTest {

    @Mock
    UserStatisticsRepository userStatisticsRepository;

    @Mock
    UserService userService;

    @Mock
    RabbitTemplate rabbitTemplate;

    @InjectMocks
    UserStatisticsService userStatisticsService;

    private User user;
    private UserStatistics userStatistics;


    @BeforeEach
    void setUp() {
        userStatistics = UserStatistics
                .builder()
                .id(1L)
                .build();
        user = User
                .builder()
                .id(1L)
                .name("Jamal")
                .login("Jamal445")
                .password("StrongPassword")
                .email("jamal@email.com")
                .userStatistics(userStatistics)
                .build();
    }

    @Test
    void getUserStatisticsByUserId_IdDoesExists_ReturnsUserStatistics(){
        // precondition
        Long id = user.getId();
        given(userService.getUser(id)).willReturn(user);

        // action
        UserStatistics returnedStat = userStatisticsService.getUserStatisticsByUserId(id);

        // verify
        assertThat(returnedStat).isEqualTo(userStatistics);
    }

    @Test
    void increaseClickCountByUserId_IdDoesExists_IncreaseClickCount(){
        // precondition
        Long id = user.getId();
        given(userService.getUser(id)).willReturn(user);

        // action
        userStatisticsService.increaseClickCountByUserId(id);

        // verify
        verify(userStatisticsRepository, times(1))
                .increaseClickCount(userStatistics.getId());
    }

    @Test
    void clickToQueue_UserNameDoesExists_AddedToQueue(){
        // precondition
        String username = user.getUsername();
        given(userService.getUserByUsername(eq(username))).willReturn(user);

        // action
        userStatisticsService.clickToQueue(username);

        // verify
        verify(rabbitTemplate, times(1))
                .convertAndSend(eq(RabbitMQConfig.EXCHANGE_NAME), eq("routing.key"), eq(user.getId()));
    }
}
