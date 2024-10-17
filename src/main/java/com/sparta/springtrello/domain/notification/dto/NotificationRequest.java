package com.sparta.springtrello.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequest {
    private String message;
    private String type;

}
