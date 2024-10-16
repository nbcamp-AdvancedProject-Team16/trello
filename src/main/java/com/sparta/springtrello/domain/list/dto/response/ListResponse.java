package com.sparta.springtrello.domain.list.dto.response;

import com.sparta.springtrello.domain.card.dto.CardResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
    private Long listId;
    private String title;
    private int order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<CardResponse> cards;

    public ListResponse(Long listId, String title, int order, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.listId = listId;
        this.title = title;
        this.order = order;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
