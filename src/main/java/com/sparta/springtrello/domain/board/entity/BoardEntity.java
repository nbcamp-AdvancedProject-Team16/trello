package com.sparta.springtrello.domain.board.entity;

import com.sparta.springtrello.domain.board.dto.request.BoardRequest;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import com.sparta.springtrello.domain.workspace.entity.WorkspaceEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "boards")
public class BoardEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "background_color", length = 7)
    private String backgroundColor;

    @Column(name = "background_image_url", length = 255)
    private String backgroundImageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "workspace_id", nullable = false)
    private WorkspaceEntity workspace;

    // 관계 설정: Board 는 여러 List 를 가질 수 있음
    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ListEntity> lists = new ArrayList<>();

    public BoardEntity(String title, String backgroundColor, String backgroundImageUrl, WorkspaceEntity workspace) {
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.workspace = workspace;
    }

    public void update(BoardRequest boardRequest) {
        this.title = boardRequest.getTitle();
        this.backgroundColor = boardRequest.getBackgroundColor() != null ? boardRequest.getBackgroundColor() : this.backgroundColor;
        this.backgroundImageUrl = boardRequest.getBackgroundImageUrl();
    }
}
