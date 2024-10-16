package com.sparta.springtrello.domain.card.entity;

import com.sparta.springtrello.domain.assignee.entity.AssigneeEntity;
import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.card.dto.CardRequest;
import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "cards", indexes ={
        @Index(name = "idx_title", columnList = "title"),
        @Index(name = "idx_dueDate", columnList = "due_date")
})
public class CardEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "due_date")
    private LocalDate dueDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "list_id",nullable = false)
    private ListEntity list;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AssigneeEntity> assigneeEntityList;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> commentEntityList;

    @OneToMany(mappedBy = "card", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<AttachmentEntity> attachments;// 추가된 부분

    @Version
    private Long version; // 동시성 제어를 위한 @Version 필드 추가

    public CardEntity(CardRequest cardRequest,ListEntity list){
        this.title = cardRequest.getTitle();
        this.description = cardRequest.getDescription();
        this.dueDate = cardRequest.getDueDate();
        this.list = list;
    }

    public void listConnect(ListEntity list) {
        this.list = list;
    }

    public void updateCard(CardRequest cardRequest) {
        this.title = cardRequest.getTitle();
        this.description = cardRequest.getDescription();
        this.dueDate = cardRequest.getDueDate();
    }
}
