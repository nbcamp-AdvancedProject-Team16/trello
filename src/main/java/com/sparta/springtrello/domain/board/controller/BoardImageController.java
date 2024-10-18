package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.service.BoardImageService;
import com.sparta.springtrello.domain.common.dto.UploadFileResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards/images")
public class BoardImageController {

    private final BoardImageService boardImageService;

    // 이미지 업로드 메서드
    @PostMapping
    public ResponseEntity<UploadFileResponse> uploadBoardImage(@RequestParam("image") MultipartFile image) {
        // 업로드된 파일의 메타데이터 응답
        UploadFileResponse uploadFileResponse = boardImageService.upload(image);
        return new ResponseEntity<>(uploadFileResponse, HttpStatus.OK);
    }

    // 이미지 삭제 메서드
    @DeleteMapping
    public ResponseEntity<String> deleteBoardImage(@RequestParam("imageUrl") String imageUrl) {
        // 이미지 삭제 처리
        boardImageService.deleteBackgroundImage(imageUrl);
        return new ResponseEntity<>("이미지가 성공적으로 삭제되었습니다.", HttpStatus.OK);
    }
}

