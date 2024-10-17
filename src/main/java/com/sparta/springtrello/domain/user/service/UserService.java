package com.sparta.springtrello.domain.user.service;

import com.sparta.springtrello.domain.common.exception.CustomException;
import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
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
@Transactional(readOnly = true)
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    public UserResponse getUser(Long userId) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new CustomException(404, "유저를 찾을 수 없습니다."));
        return new UserResponse(user.getId(), user.getEmail());
    }

    @Transactional
    public void changePassword(CustomUserDetails authUser, UserChangePasswordRequest userChangePasswordRequest) {
        UserEntity.fromAuthUser(authUser);
        validateNewPassword(userChangePasswordRequest);

        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(404, "유저를 찾을 수 없습니다."));

        if (passwordEncoder.matches(userChangePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new CustomException(403, "새 비밀번호는 기존 비밀번호와 같을 수 없습니다.");
        }

        if (!passwordEncoder.matches(userChangePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new CustomException(400, "잘못된 비밀번호입니다.");
        }

        user.changePassword(passwordEncoder.encode(userChangePasswordRequest.getNewPassword()));
    }

    private static void validateNewPassword(UserChangePasswordRequest userChangePasswordRequest) {
        if (userChangePasswordRequest.getNewPassword().length() < 8 ||
                !userChangePasswordRequest.getNewPassword().matches(".*\\d.*") ||
                !userChangePasswordRequest.getNewPassword().matches(".*[A-Z].*")) {
            throw new CustomException(400, "새 비밀번호는 8자 이상이어야 하고, 숫자와 대문자를 포함해야 합니다.");
        }
    }

    @Transactional
    public void deleteUser(CustomUserDetails authUser, UserDeleteRequest userDeleteRequest) {
        UserEntity.fromAuthUser(authUser);
        // 1. 유저 존재 여부 확인
        UserEntity user = userRepository.findById(authUser.getId())
                .orElseThrow(() -> new CustomException(404, "유저를 찾을 수 없습니다."));

        // 2. 비밀번호 일치 여부 확인
        if (!passwordEncoder.matches(userDeleteRequest.getPassword(), user.getPassword())) {
            throw new CustomException(400, "잘못된 비밀번호입니다.");
        }

        // 3. 계정 삭제 처리
        userRepository.delete(user);
    }
}
