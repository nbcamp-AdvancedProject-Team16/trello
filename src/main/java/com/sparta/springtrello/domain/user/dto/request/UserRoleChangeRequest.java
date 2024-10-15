package com.sparta.springtrello.domain.user.dto.request;

import com.sparta.springtrello.domain.user.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserRoleChangeRequest {
    private String role;
}
