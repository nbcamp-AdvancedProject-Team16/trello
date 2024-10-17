package com.sparta.springtrello.domain.card.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class CardRequest {
    @NotBlank
    private String title;

    private String description;
    private LocalDate dueDate;
}
