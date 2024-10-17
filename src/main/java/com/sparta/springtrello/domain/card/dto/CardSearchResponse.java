package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardSearchResponse {

    private long id;
    private String title;
    private String description;
    private LocalDate dueDate;
    private List<AssigneeResponse> assignees;

    public CardSearchResponse(long id, String title) {
        this.id = id;
        this.title = title;
    }

    public CardSearchResponse(long id, String title, String description, LocalDate dueDate, List<AssigneeResponse> assignees) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.dueDate = dueDate;
        this.assignees = assignees;
    }
}
