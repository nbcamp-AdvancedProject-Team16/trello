package com.sparta.springtrello.domain.member.controller;

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
    public ResponseEntity<MemberResponse> addMember(
            @PathVariable Long userId,
            @PathVariable Long workspaceId,
            @RequestBody MemberRoleSaveRequest memberRoleSaveRequest) {
        MemberResponse response = memberService.addMember(userId, workspaceId, memberRoleSaveRequest);
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{memberId}/role")
    public ResponseEntity<MemberResponse> changeMemberRole(
            @PathVariable Long userId,
            @PathVariable Long workspaceId,
            @PathVariable Long memberId,
            @RequestBody MemberRoleChangeRequest memberRoleChangeRequest) {
        MemberResponse response = memberService.changeRole(userId, workspaceId, memberId, memberRoleChangeRequest);
        return ResponseEntity.ok(response);
    }
}
