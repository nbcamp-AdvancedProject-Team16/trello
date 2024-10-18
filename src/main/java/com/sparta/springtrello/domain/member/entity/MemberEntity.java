package com.sparta.springtrello.domain.member.entity;

import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.enums.MemberRole;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "members")
public class MemberEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MemberRole memberRole;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    public MemberEntity (UserEntity user, WorkspaceEntity workspace, MemberRole memberRole) {
        this.user = user;
        this.workspace = workspace;
        this.memberRole = memberRole;
    }

    public void changeRole(MemberRole memberRole){
        this.memberRole = memberRole;
    }
}
