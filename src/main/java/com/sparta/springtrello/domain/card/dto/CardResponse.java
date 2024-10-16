package com.sparta.springtrello.domain.card.dto;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class CardResponse {
    private String title;
    private String description;
    private LocalDate dueDate;

    public CardResponse(String title, String description, LocalDate dueDate) {
        this.title = getTitle();
        this.description = getDescription();
        this.dueDate = getDueDate();
    }
}
