package com.sparta.springtrello.domain.workspace.service;

import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.CustomUserDetails;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.enums.UserRole;
import com.sparta.springtrello.domain.user.repository.UserRepository;
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
@Transactional(readOnly = true)
public class WorkspaceService {

    private final WorkspaceRepository workspaceRepository;
    private final MemberRepository memberRepository;

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
        UserEntity.fromAuthUser(authUser);

        // 1. 유저가 가입된 멤버 엔티티 조회
        List<MemberEntity> memberList = memberRepository.findAllByUserId(authUser.getId());

        // 2. 멤버 엔티티에서 워크스페이스 목록 추출 및 변환
        return memberList.stream()
                .map(member -> new WorkspaceNameResponse(member.getWorkspace().getName()))
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

        workspaceRepository.deleteById(workspaceId);
    }
}
