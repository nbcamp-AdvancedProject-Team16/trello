package com.sparta.springtrello.domain.auth.service;

import com.sparta.springtrello.config.JwtUtil;
import com.sparta.springtrello.domain.auth.dto.request.SigninRequest;
import com.sparta.springtrello.domain.auth.dto.request.SignupRequest;
import com.sparta.springtrello.domain.auth.dto.response.SigninResponse;
import com.sparta.springtrello.domain.auth.dto.response.SignupResponse;
import com.sparta.springtrello.domain.auth.exception.AuthException;
import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final NotificationService notificationService;

    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {

        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new CustomException(400, "이미 존재하는 이메일입니다.");
        }

        String encodedPassword = passwordEncoder.encode(signupRequest.getPassword());

        UserRole userRole = UserRole.of(signupRequest.getUserRole());

        UserEntity newUser = new UserEntity(
                signupRequest.getUsername(),
                signupRequest.getEmail(),
                encodedPassword,
                userRole
        );
        UserEntity savedUser = userRepository.save(newUser);

        String bearerToken = jwtUtil.createToken(savedUser.getId(), savedUser.getEmail(), userRole);

        // 슬랙 알림 전송
// 슬랙 알림 전송
        String message = String.format("%s님이 회원가입했습니다. 이메일: %s", savedUser.getUsername(), savedUser.getEmail());
        notificationService.createNotification(message, "auth"); // workspaceId를 null로 설정

        return new SignupResponse(bearerToken);
    }

    public SigninResponse signin(SigninRequest signinRequest) {
        UserEntity user = userRepository.findByEmail(signinRequest.getEmail()).orElseThrow(
                () -> new CustomException(403, "가입되지 않은 유저입니다."));

        // 로그인 시 이메일과 비밀번호가 일치하지 않을 경우 401을 반환합니다.
        if (!passwordEncoder.matches(signinRequest.getPassword(), user.getPassword())) {
            throw new CustomException(400, "잘못된 비밀번호입니다.");
        }

        String bearerToken = jwtUtil.createToken(user.getId(), user.getEmail(), user.getUserRole());

        // 슬랙 알림 전송
        String message = String.format("%s님이 로그인했습니다. 이메일: %s", user.getUsername(), user.getEmail());
        notificationService.createNotification(message, "auth"); // 알림 생성

        return new SigninResponse(bearerToken);
    }
}
