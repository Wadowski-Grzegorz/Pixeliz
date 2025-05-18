package com.example.backend.listener;

import com.example.backend.config.RabbitMQConfig;
import com.example.backend.service.UserStatisticsService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Component
public class RabbitMQConsumer {

    private final UserStatisticsService userStatisticsService;

    public RabbitMQConsumer(UserStatisticsService userStatisticsService) {
        this.userStatisticsService = userStatisticsService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void listen(Long userId) {
        userStatisticsService.increaseClickCountByUserId(userId);
    }
}