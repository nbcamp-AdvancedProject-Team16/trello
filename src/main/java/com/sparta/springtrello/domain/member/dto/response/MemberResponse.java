package com.sparta.springtrello.domain.member.dto.response;

import com.sparta.springtrello.domain.member.enums.MemberRole;
import lombok.Getter;

@Getter
public class MemberResponse {
    private final Long id;
    private final Long userId;
    private final MemberRole role;

    public MemberResponse(Long id, Long userId, MemberRole role) {
        this.id = id;
        this.userId = userId;
        this.role = role;
    }
}
