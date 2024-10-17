package com.sparta.springtrello.domain.workspace.dto.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDateTime;

@Getter
@RequiredArgsConstructor
public class WorkspaceResponse {

    private final long workspaceId;
    private final String workspaceName;
    private final String workspaceDescription;
    private final long createdBy;
    private final LocalDateTime createdAt;
    private final LocalDateTime updatedAt;
}
