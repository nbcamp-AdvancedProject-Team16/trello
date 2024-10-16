package com.sparta.springtrello.domain.notification.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NotificationRequest {
    private String message;
    private Long userId; // 실제 UserEntity ID
    private Long workspaceId; // 실제 WorkspaceEntity ID
    private String type;

}
