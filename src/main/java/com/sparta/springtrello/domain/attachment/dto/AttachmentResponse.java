package com.sparta.springtrello.domain.attachment.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class AttachmentResponse {
    private int status;
    private List<AttachmentRequest> attachments; // 첨부파일 리스트
}