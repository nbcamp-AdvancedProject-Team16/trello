package com.sparta.springtrello.domain.card.entity;

import com.sparta.springtrello.domain.comment.entity.CommentEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.list.entity.ListEntity;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Entity
@Getter
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private MemberEntity createdBy;

    @OneToMany(mappedBy = "card", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CommentEntity> comments;

}
