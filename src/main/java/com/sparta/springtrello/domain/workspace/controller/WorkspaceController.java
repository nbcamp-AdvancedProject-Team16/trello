package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceNameResponse;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/workspaces")
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/admin")
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.createWorkspace(
                authUser,
                workspaceRequest
        );
        return ResponseEntity.ok(workspaceResponse);
    }

    @GetMapping
    public ResponseEntity<List<WorkspaceNameResponse>> getWorkspaces(
            @AuthenticationPrincipal CustomUserDetails authUser
    ){
        List<WorkspaceNameResponse> workspaceNameResponses = workspaceService.getWorkspaces(authUser);
        return ResponseEntity.ok(workspaceNameResponses);
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.updateWorkspace(
                authUser,
                workspaceId,
                workspaceRequest
        );
        return ResponseEntity.ok(workspaceResponse);
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId
    ) {
        workspaceService.deleteWorkspace(authUser, workspaceId);
        return ResponseEntity.ok(null);
    }
}
