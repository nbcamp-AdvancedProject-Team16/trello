package com.sparta.springtrello.domain.list.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ListRequest {
    private String title;
    private int listOrder;
}
