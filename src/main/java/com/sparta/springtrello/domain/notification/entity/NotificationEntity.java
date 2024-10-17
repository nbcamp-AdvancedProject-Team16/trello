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

    @Column(nullable = false, length = 500)
    private String message;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = true)
//    private UserEntity user;
//
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "workspace_id", nullable = true)
//    private WorkspaceEntity workspace;

    @Column(name = "type", nullable = false)
    private String type; // 예: "comment", "member_add" 등

    // 새로운 메서드: 기본값을 설정하는 정적 메서드
    public static NotificationEntity createWithDefaults(String message, String type) {
        NotificationEntity notification = new NotificationEntity();
        notification.message = message;

        notification.type = type;
        return notification;
    }
}