package com.sparta.springtrello.domain.user.controller;

import com.sparta.springtrello.domain.user.dto.request.UserRoleChangeRequest;
import com.sparta.springtrello.domain.user.service.UserAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserAdminController {
    private final UserAdminService userAdminService;
    //@Secured
    @PatchMapping("/admin/users/{userId}")
    public void changeUserRole(@PathVariable long userId, @RequestBody UserRoleChangeRequest userRoleChangeRequest) {
        userAdminService.changeUserRole(userId, userRoleChangeRequest);
    }
}
