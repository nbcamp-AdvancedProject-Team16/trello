package com.sparta.springtrello.domain.assignee.entity;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.user.entity.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "assignee")
public class AssigneeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "card", nullable = false)
    private CardEntity card;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user", nullable = false)
    private UserEntity user;

    @Column(name = "assigned_at", nullable = false)
    private LocalDateTime assignedAt;

    public AssigneeEntity(CardEntity card, UserEntity user) {
        this.card = card;
        this.user = user;
        this.assignedAt = LocalDateTime.now();
    }

}
