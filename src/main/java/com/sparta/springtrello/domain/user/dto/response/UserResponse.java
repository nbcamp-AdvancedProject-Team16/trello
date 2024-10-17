package com.sparta.springtrello.domain.user.dto.response;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String email;

    public UserResponse(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
