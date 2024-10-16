package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceNameResponse;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long userId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.createWorkspace(
                userId,
                authUser,
                workspaceRequest
        );
        // return ResponseEntity.status(HttpStatus.CREATED).body(workspaceResponse);
        return ResponseEntity.ok(workspaceResponse);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceNameResponse>> getWorkspaces(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long userId
    ){
        List<WorkspaceNameResponse> workspaceNameResponses = workspaceService.getWorkspaces(
                userId,
                authUser
        );
        return ResponseEntity.ok(workspaceNameResponses);
    }

    @PatchMapping("/workspaces/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long userId,
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.updateWorkspace(
                userId,
                authUser,
                workspaceId,
                workspaceRequest
        );
        return ResponseEntity.ok(workspaceResponse);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long userId,
            @PathVariable Long workspaceId
    ) {
        workspaceService.deleteWorkspace(userId, authUser, workspaceId);
        return ResponseEntity.ok(null);
    }
}
