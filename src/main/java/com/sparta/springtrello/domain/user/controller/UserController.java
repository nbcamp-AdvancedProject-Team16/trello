package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.user.dto.request.UserChangePasswordRequest;
import com.sparta.springtrello.domain.user.dto.request.UserDeleteRequest;
import com.sparta.springtrello.domain.user.dto.response.UserResponse;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getUser(@PathVariable long userId) {
        return ResponseEntity.ok(userService.getUser(userId));
    }

    @PutMapping
    public void changePassword(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody UserChangePasswordRequest userChangePasswordRequest) {
        userService.changePassword(authUser, userChangePasswordRequest);
    }

    @DeleteMapping
    public void deleteUser(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @RequestBody UserDeleteRequest userDeleteRequest) {
        userService.deleteUser(authUser, userDeleteRequest);
    }
}
