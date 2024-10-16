package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardRequest {
    private String title;
    private String description;
    private LocalDate dueDate;
}
