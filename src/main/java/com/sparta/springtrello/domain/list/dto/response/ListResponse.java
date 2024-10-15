package com.sparta.springtrello.domain.list.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListResponse {
    private Long listId;
    private String title;
    private int order;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
