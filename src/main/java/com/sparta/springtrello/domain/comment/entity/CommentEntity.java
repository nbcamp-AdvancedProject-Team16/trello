package com.sparta.springtrello.domain.comment.entity;

import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.comment.dto.CommentRequest;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import com.sparta.springtrello.domain.member.entity.MemberEntity;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "comments")
public class CommentEntity extends Timestamped {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "text", nullable = false)
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card_id", nullable = false)
    private CardEntity card;

    public CommentEntity(CommentRequest commentRequest,CardEntity card){
        this.text = commentRequest.getText();
        this.card = card;
    }
    public void update(CommentRequest commentRequest) {
        this.text = commentRequest.getText();
    }

    public boolean isCreatedBy(UserEntity user) {

        return this.getId().equals(user.getId());
    }
}
