package com.sparta.springtrello.domain.attachment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SuccessResponse {
    private int status;
    private String message;
}