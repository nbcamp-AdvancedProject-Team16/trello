package com.sparta.springtrello.domain.member.entity;

import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.enums.MemberRoleType;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.userdetails.User;

@Getter
@NoArgsConstructor
@Entity
public class MemberEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRoleType memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    public MemberEntity (UserEntity user, WorkspaceEntity workspace, MemberRoleType memberRole) {
        this.user = user;
        this.workspace = workspace;
        this.memberRole = memberRole;
    }

}
