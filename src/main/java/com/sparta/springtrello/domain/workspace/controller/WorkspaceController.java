package com.sparta.springtrello.domain.workspace.controller;

import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceNameResponse;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.springtrello.domain.workspace.service.WorkspaceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping("/workspaces")
    public ResponseEntity<WorkspaceResponse> createWorkspace(
            // TODO [1] @Authentication AuthUser authUser,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.createWorkspace(
                // TODO [2] authUser,
                workspaceRequest
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(workspaceResponse);
    }

    @GetMapping("/workspaces")
    public ResponseEntity<List<WorkspaceNameResponse>> getWorkspaces(
            // TODO [5] @Authentication AuthUser authUser,
    ){
        List<WorkspaceNameResponse> workspaceNameResponses = workspaceService.getWorkspaces(
                // TODO [6] authUser
        );
        return ResponseEntity.ok(workspaceNameResponses);
    }

    @PatchMapping("/workspaces/{workspaceId}")
    public ResponseEntity<WorkspaceResponse> updateWorkspace(
            // TODO [7] @Authentication AuthUser authUser,
            @PathVariable Long workspaceId,
            @Valid @RequestBody WorkspaceRequest workspaceRequest
    ){
        WorkspaceResponse workspaceResponse = workspaceService.updateWorkspace(
                // TODO [8] authUser,
                workspaceId,
                workspaceRequest
        );
        return ResponseEntity.ok(workspaceResponse);
    }

    @DeleteMapping("/workspaces/{workspaceId}")
    public ResponseEntity<Void> deleteWorkspace(
            // TODO [11] @Authentication AuthUser authUser,
            @PathVariable Long workspaceId) {

    }
}
