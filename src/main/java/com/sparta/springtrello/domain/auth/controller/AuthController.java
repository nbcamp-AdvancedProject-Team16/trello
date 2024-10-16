package com.sparta.springtrello.domain.auth.controller;

import com.sparta.springtrello.domain.auth.dto.request.SigninRequest;
import com.sparta.springtrello.domain.auth.dto.request.SignupRequest;
import com.sparta.springtrello.domain.auth.dto.response.SigninResponse;
import com.sparta.springtrello.domain.auth.dto.response.SignupResponse;
import com.sparta.springtrello.domain.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/auth/signup") // 회원가입
    public SignupResponse signup(@Valid @RequestBody SignupRequest signupRequest) {
        return authService.signup(signupRequest);
    }

    @PostMapping("/auth/signin") // 로그인
    public SigninResponse signin(@Valid @RequestBody SigninRequest signinRequest) {
        return authService.signin(signinRequest);
    }
}
