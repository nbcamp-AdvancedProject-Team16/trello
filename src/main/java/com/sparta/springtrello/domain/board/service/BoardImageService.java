package com.sparta.springtrello.domain.board.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.sparta.springtrello.domain.common.dto.UploadFileResponse;
import com.sparta.springtrello.domain.common.service.S3UploadService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Slf4j
@RequiredArgsConstructor
@Service
public class BoardImageService {

    private final S3UploadService s3UploadService;

    public UploadFileResponse upload(MultipartFile image) {
        if (image.isEmpty() || image.getOriginalFilename() == null) {
            throw new IllegalArgumentException("이미지를 찾을 수 없습니다.");
        }

        return this.uploadImage(image);
    }

    public UploadFileResponse uploadImage(MultipartFile image) {
        this.validateImageFileException(image.getOriginalFilename());

        try {
            // S3에 업로드하고, 업로드된 파일의 메타데이터 반환
            return s3UploadService.uploadImageToS3(image);
        } catch (IOException e) {
            throw new RuntimeException("이미지를 업로드할 수 없습니다.", e);
        }
    }

    public void validateImageFileException(String fileName) {
        int lastDotIndex = fileName.lastIndexOf('.');
        if (lastDotIndex == -1) {
            throw new IllegalArgumentException("이미지를 찾을 수 없습니다.");
        }

        String extension = fileName.substring(lastDotIndex + 1).toLowerCase();
        List<String> allowedExtensionList = Arrays.asList("jpg", "jpeg", "png", "gif");

        if (!allowedExtensionList.contains(extension)) {
            throw new IllegalArgumentException("적절한 이미지 파일이 아닙니다.");
        }
    }

    public void deleteBackgroundImage(String imageUrl) {
        s3UploadService.deleteImageFromS3(imageUrl);
    }
}
