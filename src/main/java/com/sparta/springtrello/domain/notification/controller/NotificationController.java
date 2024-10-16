package com.sparta.springtrello.domain.notification.controller;

import com.sparta.springtrello.domain.notification.dto.CommonResponse;
import com.sparta.springtrello.domain.notification.dto.NotificationRequest;
import com.sparta.springtrello.domain.notification.dto.NotificationResponse;
import com.sparta.springtrello.domain.notification.dto.NotificationSettingRequest;
import com.sparta.springtrello.domain.notification.entity.NotificationEntity;
import com.sparta.springtrello.domain.notification.entity.NotificationSettingEntity;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationProducer;
    private final NotificationService notificationService; // 알림 서비스 추가
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    // 알림 생성
    @PostMapping
    public ResponseEntity<NotificationResponse> createNotification(@RequestBody NotificationRequest request) {
        UserEntity user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("사용자를 찾을 수 없습니다."));

        WorkspaceEntity workspace = workspaceRepository.findById(request.getWorkspaceId())
                .orElseThrow(() -> new RuntimeException("작업공간을 찾을 수 없습니다."));

        NotificationEntity notification = notificationProducer.createNotification(
                request.getMessage(),
                user,
                workspace,
                request.getType()
        );

        NotificationResponse notificationDTO = new NotificationResponse(
                notification.getId(),
                notification.getType(),
                notification.getMessage(),
                notification.isRead(),
                notification.getCreatedAt()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(notificationDTO);
    }

    // 알림 조회
    @GetMapping
    public ResponseEntity<CommonResponse<List<NotificationResponse>>> getAllNotifications() {
        List<NotificationResponse> notificationResponses = notificationService.getAllNotifications();
        CommonResponse<List<NotificationResponse>> response = new CommonResponse<>(200, notificationResponses);
        return ResponseEntity.ok(response);
    }

    // 알림 읽음 처리
    @PatchMapping("/{notificationId}/read")
    public ResponseEntity<CommonResponse<String>> markAsRead(@PathVariable Long notificationId) {
        notificationService.markAsRead(notificationId);
        CommonResponse<String> response = new CommonResponse<>(200, "Notification marked as read.");
        return ResponseEntity.ok(response);
    }

    // 알림 삭제
    @DeleteMapping("/{notificationId}")
    public ResponseEntity<CommonResponse<String>> deleteNotification(@PathVariable Long notificationId) {
        notificationService.deleteNotification(notificationId);
        CommonResponse<String> response = new CommonResponse<>(200, "Notification deleted successfully.");
        return ResponseEntity.ok(response);
    }

    // 알림 설정 조회
    @GetMapping("/settings")
    public ResponseEntity<CommonResponse<NotificationSettingRequest>> getNotificationSettings() {
        NotificationSettingEntity settings = notificationService.getNotificationSettings();
        NotificationSettingRequest dto = new NotificationSettingRequest(settings.getType(), settings.isEnabled());
        CommonResponse<NotificationSettingRequest> response = new CommonResponse<>(200, dto);
        return ResponseEntity.ok(response);
    }

    // 알림 설정 업데이트
    @PatchMapping("/settings")
    public ResponseEntity<CommonResponse<String>> updateNotificationSettings(@RequestBody NotificationSettingRequest request) {
        // 검증 및 업데이트 로직
        notificationService.updateNotificationSettings(new NotificationSettingEntity(request.getType(), request.isEnabled()));
        CommonResponse<String> response = new CommonResponse<>(200, "Notification settings updated.");
        return ResponseEntity.ok(response);
    }
}