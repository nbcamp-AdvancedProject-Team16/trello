package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Getter
@RequiredArgsConstructor
public class CardSearchResponse {

    private final String title;
    private final String description;
    private final LocalDate dueDate;
    private List<AssigneeResponse> assigneeResponseList;

}
