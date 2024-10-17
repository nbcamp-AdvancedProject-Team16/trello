package com.sparta.springtrello.domain.user.service;

import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.dto.request.UserChangePasswordRequest;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequest;
import com.sparta.springtrello.domain.user.dto.response.UserResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final NotificationService notificationService;


    public UserResponse getUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));

        // 비밀번호 변경 후 슬랙 알림 전송
        String message = String.format("%s님의 정보를 조회했습니다.", user.getEmail());

        // 알림 생성
        notificationService.createNotification(message, "user");

        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(CustomUserDetails authUser, UserChangePasswordRequest userChangePasswordRequest) {
        UserEntity.fromAuthUser(authUser);
        validateNewPassword(userChangePasswordRequest);

        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new InvalidRequestException("새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        // 비밀번호 변경 후 슬랙 알림 전송
        String message = String.format("%s님이 비밀번호를 변경했습니다.", user.getEmail());

        // 알림 생성
        notificationService.createNotification(message, "user");

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new InvalidRequestException("새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    @Transactional
    public void deleteUser(CustomUserDetails authUser, UserDeleteRequest userDeleteRequest) {
        UserEntity.fromAuthUser(authUser);
        // 1. 유저 존재 여부 확인
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(userDeleteRequest.getPassword(), user.getPassword())) {
            throw new InvalidRequestException("잘못된 비밀번호입니다.");
        }

        // 사용자 삭제 후 슬랙 알림 전송
        String message = String.format("%s님이 계정을 삭제했습니다.", user.getEmail());
        notificationService.createNotification(message, "user");

        // 3. 계정 삭제 처리
        userRepository.delete(user);
    }
}
