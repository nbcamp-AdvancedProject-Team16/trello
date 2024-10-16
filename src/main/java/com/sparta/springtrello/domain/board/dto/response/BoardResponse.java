package com.sparta.springtrello.domain.board.dto.response;

import com.sparta.springtrello.domain.list.dto.response.ListResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class BoardResponse {
    private Long id;
    private String title;
    private String backgroundColor;
    private String backgroundImageUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private List<ListResponse> lists;

    public BoardResponse(Long id, String title, String backgroundColor, String backgroundImageUrl, LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.backgroundColor = backgroundColor;
        this.backgroundImageUrl = backgroundImageUrl;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
