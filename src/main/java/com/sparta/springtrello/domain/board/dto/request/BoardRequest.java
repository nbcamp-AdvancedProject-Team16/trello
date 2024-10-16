package com.sparta.springtrello.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardRequest {
    @NotNull
    private String title;

    private String backgroundColor;
    private String backgroundImageUrl;
}
