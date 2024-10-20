package com.sparta.springtrello.domain.list.entity;

import com.sparta.springtrello.domain.board.entity.BoardEntity;
import com.sparta.springtrello.domain.card.entity.CardEntity;
import com.sparta.springtrello.domain.common.entity.Timestamped;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@NoArgsConstructor
@Table(name = "lists")
public class ListEntity extends Timestamped {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(name = "list_order")
    private Integer listOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private BoardEntity board;

    // 관계 설정: List는 여러 Card를 가질 수 있음
    @OneToMany(mappedBy = "list", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CardEntity> cards = new ArrayList<>();

    public ListEntity(String title, Integer listOrder, BoardEntity board) {
        this.title = title;
        this.listOrder = listOrder;
        this.board = board;
    }

    public void update(String title, int listOrder) {
        this.title = title;
        this.listOrder = listOrder;
    }

    public void setListOrder(int listOrder) {
        this.listOrder = listOrder;
    }
}
