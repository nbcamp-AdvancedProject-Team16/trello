package com.sparta.springtrello.domain.notification.dto;

import com.sparta.springtrello.domain.notification.entity.NotificationEntity;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class NotificationResponse {
    private Long id;
    private String type; // 타입 추가
    private String message;
    private boolean isRead; // 읽음 여부 추가
    private LocalDateTime createdAt; // 생성 날짜

    public NotificationResponse(NotificationEntity notification) {
        this.id = notification.getId();
        this.message = notification.getMessage();
        this.createdAt = notification.getCreatedAt();
        this.isRead = notification.isRead();
        this.type = notification.getType();
    }

    public NotificationResponse(Long id, String type, String message, boolean read, LocalDateTime createdAt) {
        this.id = id;
        this.message = message;
        this.type = type;
        this.createdAt = createdAt;
        this.isRead = read;
    }
}
