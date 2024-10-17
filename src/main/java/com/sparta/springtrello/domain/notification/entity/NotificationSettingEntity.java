package com.sparta.springtrello.domain.notification.entity;

import jakarta.persistence.*;
        import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "notificationsettings")
@NoArgsConstructor
public class NotificationSettingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String type; // 슬랙 등
    private boolean enabled;

    // 추가 생성자
    public NotificationSettingEntity(String type, boolean enabled) {
        this.type = type;
        this.enabled = enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}