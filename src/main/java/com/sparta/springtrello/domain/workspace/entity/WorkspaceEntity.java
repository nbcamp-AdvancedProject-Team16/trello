package com.sparta.springtrello.domain.workspace.entity;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.dto.request.WorkspaceRequest;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "workspaces")
public class WorkspaceEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "workspace_name", nullable = false)
    private String name;

    @Column(name = "workspace_description", nullable = false)
    private String description;

    @Column(name = "created_by")
    private long createdBy;

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BoardEntity> boards = new ArrayList<>();

    @OneToMany(mappedBy = "workspace", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MemberEntity> members = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "user_id", nullable = false)
//    private UserEntity user;

    public WorkspaceEntity(String name, String description, long createdBy) {
        this.name = name;
        this.description = description;
        this.createdBy = createdBy;
    }

    public void updateWorkspace(String name, String description) {
        this.name = name;
        this.description = description;
    }
}
