package com.sparta.springtrello.domain.notification.service;


import com.sparta.springtrello.domain.notification.config.RabbitConfig;
import com.sparta.springtrello.domain.notification.dto.NotificationResponse;
import com.sparta.springtrello.domain.notification.dto.NotificationSettingResponse;
import com.sparta.springtrello.domain.notification.entity.NotificationEntity;
import com.sparta.springtrello.domain.notification.entity.NotificationSettingEntity;
import com.sparta.springtrello.domain.notification.exception.NotificationDisabledException;
import com.sparta.springtrello.domain.notification.exception.NotificationNotFoundException;
import com.sparta.springtrello.domain.notification.repository.NotificationRepository;
import com.sparta.springtrello.domain.notification.repository.NotificationSettingRepository;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationSettingRepository notificationSettingRepository;
    private final RabbitTemplate rabbitTemplate;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    // 알림 생성
    @Transactional
    public NotificationResponse createNotification(String message, Long userId, Long workspaceId, String type) {

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new RuntimeException("작업공간을 찾을 수 없습니다."));

        NotificationEntity notification = NotificationEntity.createWithDefaults(message, user, workspace, type);

        // 알림 설정 확인
        NotificationSettingEntity settings = notificationSettingRepository.findAll().stream().findFirst()
                .orElse(new NotificationSettingEntity("slack", true)); // 기본값 사용

        // 알림 설정이 활성화된 경우에만 RabbitMQ에 메시지 발송
        if (settings.isEnabled()) {
            notificationRepository.save(notification);
            rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, message);
            return new NotificationResponse(notification.getId(),
                    notification.getType(), notification.getMessage(), notification.getCreatedAt());
        } else {
            throw new NotificationDisabledException("알림이 비활성화되어 있습니다.");
        }
    }

    // 알림 조회
    public List<NotificationResponse> getAllNotifications(CustomUserDetails authUser) {

        UserEntity user = UserEntity.fromAuthUser(authUser);

        List<NotificationEntity> notifications = notificationRepository.findAll();

        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException("알림을 찾을 수 없습니다.");
        }

        return notifications.stream()
                .map(NotificationResponse::new)
                .toList();
    }


    // 알림 삭제
    @Transactional
    public void deleteNotification(Long notificationId) {
        if (!notificationRepository.existsById(notificationId)) {
            throw new NotificationNotFoundException("알림을 찾을 수 없습니다.");
        }
        notificationRepository.deleteById(notificationId);
    }

    // 알림 설정 조회
    public NotificationSettingResponse getNotificationSettings() {
        NotificationSettingEntity entity = notificationSettingRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("알림 설정을 찾을 수 없습니다.")); // 예외 처리

        // 변환 로직
        return new NotificationSettingResponse(entity.getType(), entity.isEnabled());
    }

    // 알림 방식 설정
    @Transactional
    public void updateNotificationSettings(NotificationSettingResponse newSettings) {
        NotificationSettingEntity existingSettings = notificationSettingRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No notification settings found"));

        existingSettings.setEnabled(newSettings.isEnabled());

        notificationSettingRepository.save(existingSettings);
    }


}