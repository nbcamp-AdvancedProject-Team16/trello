package com.sparta.springtrello.domain.member.controller;

import com.sparta.springtrello.domain.common.response.ApiResponse;
import com.sparta.springtrello.domain.member.dto.request.MemberRoleChangeRequest;
import com.sparta.springtrello.domain.member.dto.request.MemberRoleSaveRequest;
import com.sparta.springtrello.domain.member.dto.response.MemberResponse;
import com.sparta.springtrello.domain.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/admin/workspaces/{workspaceId}/members")
public class MemberController {

    private final MemberService memberService;

    @PostMapping
    public ResponseEntity<ApiResponse<MemberResponse>> addMember(
            @PathVariable Long userId,
            @PathVariable Long workspaceId,
            @RequestBody MemberRoleSaveRequest memberRoleSaveRequest) {
        MemberResponse response = memberService.addMember(userId, workspaceId, memberRoleSaveRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "멤버가 성공적으로 추가되었습니다..", response));
    }

    @PatchMapping("/{memberId}/role")
    public ResponseEntity<ApiResponse<MemberResponse>> changeMemberRole(
            @PathVariable Long userId,
            @PathVariable Long workspaceId,
            @PathVariable Long memberId,
            @RequestBody MemberRoleChangeRequest memberRoleChangeRequest) {
        MemberResponse response = memberService.changeRole(userId, workspaceId, memberId, memberRoleChangeRequest);
        return ResponseEntity.ok(new ApiResponse<>(200, "멤버 권한이 성공적으로 수정되었습니다.", response));
    }
}
