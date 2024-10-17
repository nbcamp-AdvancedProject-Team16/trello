package com.sparta.springtrello.domain.list.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListRequest {
    @NotBlank
    private String title;
    private int listOrder;
}
