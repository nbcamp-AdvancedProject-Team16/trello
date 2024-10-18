package com.sparta.springtrello.domain.attachment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class AttachmentRequest {
    private Long id;
    private String fileName;
    private String fileType;
    private Integer fileSize;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}