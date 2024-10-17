package com.sparta.springtrello.domain.card.dto;

import com.sparta.springtrello.domain.assignee.dto.AssigneeRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class CardRequest {
    @NotBlank
    private String title;

    private String description;
    private LocalDate dueDate;
}
