package com.sparta.springtrello.domain.comment.entity;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class CommentEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by")
    private MemberEntity createdBy;
}
