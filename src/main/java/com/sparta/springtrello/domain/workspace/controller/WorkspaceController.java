package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.common.response.ApiResponse;
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
    public ResponseEntity<ApiResponse<WorkspaceResponse>> createWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.createWorkspace(
                authUser,
                workspaceRequest
        );
        return ResponseEntity.ok(new ApiResponse<>(201, "보드가 성공적으로 수정되었습니다.", workspaceResponse));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<WorkspaceNameResponse>>> getWorkspaces(
            @AuthenticationPrincipal CustomUserDetails authUser
    ){
        List<WorkspaceNameResponse> workspaceNameResponses = workspaceService.getWorkspaces(authUser);
        return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 조회되었습니다.", workspaceNameResponses));
    }

    @PatchMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<WorkspaceResponse>> updateWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.updateWorkspace(
                authUser,
                workspaceId,
                workspaceRequest
        );
        return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 수정되었습니다.", workspaceResponse));
    }

    @DeleteMapping("/{workspaceId}")
    public ResponseEntity<ApiResponse<Void>> deleteWorkspace(
            @AuthenticationPrincipal CustomUserDetails authUser,
            @PathVariable Long workspaceId
    ) {
        workspaceService.deleteWorkspace(authUser, workspaceId);
        return ResponseEntity.ok(new ApiResponse<>(200, "보드가 성공적으로 삭제되었습니다.", null));
    }
}
