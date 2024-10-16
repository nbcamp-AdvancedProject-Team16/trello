package com.sparta.springtrello.domain.board.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class BoardRequest {
    @NotNull
    private String title;

    private String backgroundColor;
    private String backgroundImageUrl;
}
