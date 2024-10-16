package com.sparta.springtrello.domain.notification.service;


import com.sparta.springtrello.domain.notification.config.RabbitConfig;
import com.sparta.springtrello.domain.notification.dto.NotificationResponse;
import com.sparta.springtrello.domain.notification.entity.NotificationEntity;
import com.sparta.springtrello.domain.notification.entity.NotificationSettingEntity;
import com.sparta.springtrello.domain.notification.exception.NotificationDisabledException;
import com.sparta.springtrello.domain.notification.exception.NotificationNotFoundException;
import com.sparta.springtrello.domain.notification.repository.NotificationRepository;
import com.sparta.springtrello.domain.notification.repository.NotificationSettingRepository;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
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

    // 알림 생성
    @Transactional
    public NotificationEntity createNotification(String message, UserEntity user, WorkspaceEntity workspace, String type) {
        NotificationEntity notification = NotificationEntity.createWithDefaults(message, user, workspace, type);

        // 알림 설정 확인
        NotificationSettingEntity settings = notificationSettingRepository.findAll().stream().findFirst()
                .orElse(new NotificationSettingEntity("slack", true)); // 기본값 사용

        // 알림 설정이 활성화된 경우에만 RabbitMQ에 메시지 발송
        if (settings.isEnabled()) {
            notificationRepository.save(notification);
            rabbitTemplate.convertAndSend(RabbitConfig.QUEUE_NAME, message);
            return notification;
        } else {
            throw new NotificationDisabledException("알림이 비활성화되어 있습니다.");
        }
    }

    // 알림 조회
    public List<NotificationResponse> getAllNotifications() {
        List<NotificationEntity> notifications = notificationRepository.findAll();

        if (notifications.isEmpty()) {
            throw new NotificationNotFoundException("알림을 찾을 수 없습니다.");
        }

        return notifications.stream()
                .map(NotificationResponse::new)
                .toList();
    }

    // 알림 읽음 처리
    @Transactional
    public void markAsRead(Long notificationId) {
        NotificationEntity notification = notificationRepository.findById(notificationId)
                .orElseThrow(() -> new NotificationNotFoundException("알림을 찾을 수 없습니다."));
        notification.markAsRead();
        notificationRepository.save(notification);
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
    public NotificationSettingEntity getNotificationSettings() {
        return notificationSettingRepository.findAll().stream()
                .findFirst()
                .orElse(new NotificationSettingEntity("slack", true)); // 기본값 리턴
    }

    // 알림 방식 설정
    @Transactional
    public void updateNotificationSettings(NotificationSettingEntity newSettings) {
        NotificationSettingEntity existingSettings = notificationSettingRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No notification settings found"));

        existingSettings.setEnabled(newSettings.isEnabled());

        notificationSettingRepository.save(existingSettings);
    }


}