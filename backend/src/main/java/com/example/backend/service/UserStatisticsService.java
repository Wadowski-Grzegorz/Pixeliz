package com.example.backend.service;

import com.example.backend.config.RabbitMQConfig;
import com.example.backend.model.UserStatistics;
import com.example.backend.repository.UserRepository;
import com.example.backend.repository.UserStatisticsRepository;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;


@Service
public class UserStatisticsService {
    private final UserStatisticsRepository userStatisticsRepository;
    private final UserService userService;
    private final RabbitTemplate rabbitTemplate;

    public UserStatisticsService(
            UserStatisticsRepository userStatisticsRepository,
            UserRepository userRepository,
            UserService userService,
            RabbitTemplate rabbitTemplate
    ) {
        this.userStatisticsRepository = userStatisticsRepository;
        this.userService = userService;
        this.rabbitTemplate = rabbitTemplate;
    }

    public UserStatistics getUserStatisticsByUserId(Long userId) {
        return userService.getUser(userId).getUserStatistics();
    }

    public void increaseClickCountByUserId(Long userId){
        UserStatistics userStatistics = getUserStatisticsByUserId(userId);
        userStatisticsRepository.increaseClickCount(userStatistics.getId());
    }

    public void clickToQueue(Long userId){
        rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, "routing.key", userId);
    }
}
