package com.sparta.springtrello.domain.workspace.entity;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class WorkspaceEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "workspace_name", nullable = false)
    private String name;

    @Column(name = "workspace_description", nullable = false)
    private String description;

    @OneToMany(mappedBy = "workspace")
    private List<BoardEntity> boards = new ArrayList<>();

    public WorkspaceEntity(String name, String description, List<BoardEntity> boards) {
        this.name = name;
        this.description = description;
        this.boards = boards;
    }
}
