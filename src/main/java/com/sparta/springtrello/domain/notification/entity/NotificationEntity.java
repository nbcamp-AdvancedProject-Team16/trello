package com.sparta.springtrello.domain.notification.entity;

import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "notifications")
public class NotificationEntity extends Timestamped{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String status;

    @Column(nullable = false, length = 25)
    private String message;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    @Column(name = "is_read", nullable = false)
    private boolean isRead;

    @Column(name = "type", nullable = false)
    private String type; // 예: "comment", "member_add" 등

    // 새로운 메서드: 기본값을 설정하는 정적 메서드
    public static NotificationEntity createWithDefaults(String message, UserEntity user, WorkspaceEntity workspace, String type) {
        NotificationEntity notification = new NotificationEntity();
        notification.message = message;
        notification.user = user;
        notification.workspace = workspace;
        notification.type = type;
        notification.isRead = false; // 기본값 설정
        return notification;
    }

    // 새로운 메서드
    public NotificationEntity markAsRead() {
        this.isRead = true; // isRead 값을 true로 설정
        return this;
    }

}