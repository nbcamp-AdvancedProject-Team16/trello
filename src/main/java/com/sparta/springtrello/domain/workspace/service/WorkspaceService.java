package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.common.dto.AuthUser;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceNameResponse;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.exception.ApiException;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.apache.coyote.BadRequestException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public WorkspaceResponse createWorkspace(AuthUser authUser, WorkspaceRequest workspaceRequest) {

        // TODO [3] ADMIN 권한 확인
        if(!authUser.getUserRole().equals(UserRole.ADMIN.name())){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        WorkspaceEntity newWorkspace = new WorkspaceEntity(
                workspaceRequest.getName(),
                workspaceRequest.getDescription()
        );

        WorkspaceEntity savedWorkspace = workspaceRepository.save(newWorkspace);
        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getDescription(),
                // TODO [4] bearer 토큰에서 가져온 user 의 UserEntity
                savedWorkspace.getCreatedAt(),
                savedWorkspace.getUpdatedAt()
        );
    }

    public List<WorkspaceNameResponse> getWorkspaces() {
        List<WorkspaceEntity> workspaceList = workspaceRepository.findAll();

        return workspaceList.stream().map(workspaceEntity -> new WorkspaceNameResponse(
                workspaceEntity.getName()
        )).collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(AuthUser authUser, Long workspaceId, WorkspaceRequest workspaceRequest) {

        // TODO [9] ADMIN 권한 확인
        if(!authUser.getUserRole().equals(UserRole.ADMIN.name())){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        // 해당 Workspace 찾기
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 workspace 를 찾을 수 없습니다."));

        // Workspace 정보 변경
        workspace.updateWorkspace(workspaceRequest.getName(), workspaceRequest.getDescription());

        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                // TODO [10] bearer 토큰에서 가져온 user 의 memberEntity
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }

    @Transactional
    public WorkspaceResponse deleteWorkspace(AuthUser authUser, Long workspaceId) {

        // TODO [] ADMIN 권한 확인
        if(!authUser.getUserRole().equals(UserRole.ADMIN.name())){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        workspaceRepository.deleteById(workspaceId);
    }
}
