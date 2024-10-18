package com.sparta.springtrello.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationSettingRequest {
    private String type;
    private boolean enabled;
}
