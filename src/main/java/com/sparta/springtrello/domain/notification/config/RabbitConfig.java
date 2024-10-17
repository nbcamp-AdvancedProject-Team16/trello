package com.sparta.springtrello.domain.notification.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {
    public static final String QUEUE_NAME = "notificationQueue";

    // 서버가 재시작되더라도 큐의 데이터가 사라지지 않게
    @Bean
    public Queue notificationQueue() {
        return new Queue(QUEUE_NAME, true);
    }
}