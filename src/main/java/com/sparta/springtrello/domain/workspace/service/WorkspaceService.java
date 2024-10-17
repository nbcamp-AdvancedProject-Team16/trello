package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.domain.notification.service.NotificationService;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceNameResponse;
import com.sparta.springtrello.domain.workspace.dto.response.WorkspaceResponse;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.exception.ApiException;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final NotificationService notificationService; // 추가된 부분

    @Transactional
    public WorkspaceResponse createWorkspace(CustomUserDetails authUser, WorkspaceRequest workspaceRequest) {
        UserEntity.fromAuthUser(authUser);

        if(!authUser.getRole().equals(UserRole.ADMIN)){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        WorkspaceEntity newWorkspace = new WorkspaceEntity(
                workspaceRequest.getName(),
                workspaceRequest.getDescription(),
                authUser.getId()
        );

        WorkspaceEntity savedWorkspace = workspaceRepository.save(newWorkspace);


        // 슬랙 알림 전송
        String message = String.format("%s님이 새로운 워크스페이스[%s]를 생성했습니다.",
                authUser.getEmail(),
                savedWorkspace.getName());
        notificationService.createNotification(message,  "workspace");

        return new WorkspaceResponse(
                savedWorkspace.getId(),
                savedWorkspace.getName(),
                savedWorkspace.getDescription(),
                // TODO [4] bearer 토큰에서 가져온 user 의 id
                authUser.getId(),
                savedWorkspace.getCreatedAt(),
                savedWorkspace.getUpdatedAt()
        );
    }

    public List<WorkspaceNameResponse> getWorkspaces(CustomUserDetails authUser) {
        // WorkspaceRepository 의 findByMembers_UserId 메서드를 사용해 유저의 워크스페이스 목록 조회
        List<WorkspaceEntity> workspaces = workspaceRepository.findByMembers_UserId(authUser.getId());
        // WorkspaceEntity 목록을 WorkspaceNameResponse 로 변환하여 반환

        // 슬랙 알림 전송
        String message = String.format("%s님이 워크스페이스 목록을 조회했습니다.", authUser.getEmail());
        notificationService.createNotification(message,  "workspace");

        return workspaces.stream()
                .map(workspace -> new WorkspaceNameResponse(workspace.getName()))
                .collect(Collectors.toList());
    }

    @Transactional
    public WorkspaceResponse updateWorkspace(CustomUserDetails authUser, Long workspaceId, WorkspaceRequest workspaceRequest) {
        UserEntity.fromAuthUser(authUser);

        // TODO [9] ADMIN 권한 확인
        if(!authUser.getRole().equals(UserRole.ADMIN)){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        // 해당 Workspace 찾기
        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 workspace 를 찾을 수 없습니다."));

        // Workspace 정보 변경
        workspace.updateWorkspace(workspaceRequest.getName(), workspaceRequest.getDescription());

        // 슬랙 알림 전송
        String message = String.format("%s님이 워크스페이스[%s]의 정보를 수정했습니다.",
                authUser.getEmail(),
                workspace.getName());
        notificationService.createNotification(message, "workspace");


        return new WorkspaceResponse(
                workspace.getId(),
                workspace.getName(),
                workspace.getDescription(),
                // TODO [4] bearer 토큰에서 가져온 user 의 id
                authUser.getId(),
                workspace.getCreatedAt(),
                workspace.getUpdatedAt()
        );
    }

    @Transactional
    public void deleteWorkspace(CustomUserDetails authUser, Long workspaceId) {
        UserEntity.fromAuthUser(authUser);

        // TODO [] ADMIN 권한 확인
        if(!authUser.getRole().equals(UserRole.ADMIN)){
            throw new ApiException(HttpStatus.UNAUTHORIZED, "해당 권한이 없습니다.");
        }

        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(() -> new ApiException(HttpStatus.NOT_FOUND, "해당 워크스페이스가 없습니다."));

        // 슬랙 알림 전송
        String message = String.format("%s님이 워크스페이스[%s]를 삭제했습니다.",
                authUser.getEmail(),
                workspace.getName());
        notificationService.createNotification(message,  "workspace");

        workspaceRepository.deleteById(workspaceId);
    }
}
