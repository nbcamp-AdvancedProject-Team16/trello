package com.sparta.springtrello.domain.common.dto;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class AuthUser {
    private final Long id;
    private final String email;
    private final UserRole userRole;

    public AuthUser(Long id, String email, UserRole userRole) {
        this.id = id;
        this.email = email;
        this.userRole = userRole;
    }
}
