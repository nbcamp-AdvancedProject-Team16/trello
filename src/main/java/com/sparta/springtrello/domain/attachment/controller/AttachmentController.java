package com.sparta.springtrello.domain.attachment.controller;

import com.sparta.springtrello.domain.attachment.dto.AttachmentRequest;
import com.sparta.springtrello.domain.attachment.dto.AttachmentResponse;
import com.sparta.springtrello.domain.attachment.dto.SuccessResponse;
import com.sparta.springtrello.domain.attachment.entity.AttachmentEntity;
import com.sparta.springtrello.domain.attachment.service.AttachmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
@RequestMapping("/attachments")
public class AttachmentController {

    private final AttachmentService attachmentService;

    // 첨부파일 생성
    @PostMapping("/{cardId}")
    public ResponseEntity<SuccessResponse> addAttachments(@RequestParam("files") List<MultipartFile> files, @PathVariable Long cardId) {
        List<AttachmentEntity> attachments = attachmentService.addAttachments(files, cardId);
        String ids = attachments.stream()
                .map(attachment -> attachment.getId().toString())
                .collect(Collectors.joining(", "));

        return ResponseEntity.status(201)
                .body(new SuccessResponse(201, "첨부파일이 성공적으로 추가되었습니다. ID: " + ids));
    }

    @GetMapping("/{cardId}")
    public ResponseEntity<AttachmentResponse> getAttachments(@PathVariable Long cardId) {
        List<AttachmentRequest> attachments = attachmentService.getAttachmentsByCardId(cardId);

        // Response 객체 생성
        AttachmentResponse response = new AttachmentResponse(200, attachments);
        return ResponseEntity.ok(response);
    }

    // 첨부파일 삭제
    @DeleteMapping("/{attachmentId}")
    public ResponseEntity<SuccessResponse> deleteAttachment(@PathVariable Long attachmentId) {
        attachmentService.deleteAttachment(attachmentId);
        return ResponseEntity.status(200) // 상태 코드를 200으로 변경
                .body(new SuccessResponse(200, "첨부파일이 성공적으로 삭제되었습니다.")); // 메시지 포함
    }
}