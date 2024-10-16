package com.sparta.springtrello.domain.notification.service;

import com.sparta.springtrello.domain.notification.config.RabbitConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NotificationConsumer {

    private final SlackService slackService;

    @RabbitListener(queues = RabbitConfig.QUEUE_NAME)
    public void receiveMessage(String message) {
        slackService.sendSlackNotification(message);
    }
}