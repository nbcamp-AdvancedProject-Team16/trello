package com.sparta.springtrello.domain.board.controller;

import com.sparta.springtrello.domain.board.service.BoardImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/boards")
public class BoardImageController {

    private final BoardImageService boardImageService;

    @PostMapping("/upload")
    public ResponseEntity<String> uploadProfileImage(@RequestParam("image") MultipartFile image) {
        String imageUrl = boardImageService.upload(image);
        return new ResponseEntity<>(imageUrl, HttpStatus.OK);
    }

    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteProfileImage(@RequestParam("imageUrl") String imageUrl) {
        boardImageService.deleteImageFromS3(imageUrl);
        return new ResponseEntity<>("Image deleted successfully", HttpStatus.OK);
    }
}

