package com.sparta.springtrello.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class NotificationSettingResponse {
    private String type; // 슬랙 등
    private boolean enabled;
}
