package com.sparta.springtrello.domain.user.service;

import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.user.dto.request.UserRoleChangeRequest;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserAdminService {
    private final UserRepository userRepository;

    @Transactional
    public void changeUserRole(long userId, UserRoleChangeRequest userRoleChangeRequest) {
        UserEntity user = userRepository.findById(userId).orElseThrow(() -> new InvalidRequestException("User not found"));
        user.updateRole(UserRole.of(userRoleChangeRequest.getRole()));
    }
}
