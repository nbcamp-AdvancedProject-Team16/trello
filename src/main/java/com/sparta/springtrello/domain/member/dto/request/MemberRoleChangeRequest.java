package com.sparta.springtrello.domain.member.dto.request;

import com.sparta.springtrello.domain.member.enums.MemberRole;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class MemberRoleChangeRequest {
    @NotNull
    private Long userId; // 추가할 유저 ID
    @NotNull
    private MemberRole memberRole; // 할당할 역할
}

