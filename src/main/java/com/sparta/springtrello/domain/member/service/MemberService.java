package com.sparta.springtrello.domain.member.service;

import com.sparta.springtrello.domain.common.exception.InvalidRequestException;
import com.sparta.springtrello.domain.member.dto.request.MemberRoleChangeRequest;
import com.sparta.springtrello.domain.member.dto.request.MemberRoleSaveRequest;
import com.sparta.springtrello.domain.member.dto.response.MemberResponse;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.member.repository.MemberRepository;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.user.repository.UserRepository;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import com.sparta.springtrello.domain.workspace.repository.WorkspaceRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;
    private final UserRepository userRepository;
    private final WorkspaceRepository workspaceRepository;

    @Transactional
    public MemberResponse addMember(Long userId, Long workspaceId, MemberRoleSaveRequest memberRoleSaveRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        WorkspaceEntity workspace = workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new InvalidRequestException("Workspace not found"));

        MemberEntity newMember = new MemberEntity(user, workspace, memberRoleSaveRequest.getMemberRole());
        MemberEntity savedMember = memberRepository.save(newMember);

        return new MemberResponse(savedMember.getId(), user.getId(), savedMember.getMemberRole());
    }

    @Transactional
    public MemberResponse changeRole(Long userId, Long workspaceId, Long memberId, MemberRoleChangeRequest memberRoleChangeRequest) {
        userRepository.findById(userId)
                .orElseThrow(() -> new InvalidRequestException("User not found"));

        workspaceRepository.findById(workspaceId)
                .orElseThrow(()-> new InvalidRequestException("Workspace not found"));

        MemberEntity member = memberRepository.findById(memberId)
                .orElseThrow(() -> new InvalidRequestException("Member not found"));

        // 요청한 userId가 실제로 멤버와 일치하는지 확인
        if (!member.getUser().getId().equals(memberRoleChangeRequest.getUserId())) {
            throw new InvalidRequestException("권한이 없습니다. 이 멤버의 역할을 변경할 수 없습니다.");
        }

        member.changeRole(memberRoleChangeRequest.getMemberRole());

        return new MemberResponse(member.getId(), member.getUser().getId(), member.getMemberRole());
    }
}