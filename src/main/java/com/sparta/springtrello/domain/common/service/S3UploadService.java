package com.sparta.springtrello.domain.common.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.amazonaws.util.IOUtils;
import com.sparta.springtrello.domain.common.dto.UploadFileResponse;
import lombok.RequiredArgsConstructor;
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
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class S3UploadService {

    private final AmazonS3 amazonS3;

    @Value("${cloud.aws.s3.bucketName}")
    private String bucketName;

    public UploadFileResponse uploadImageToS3(MultipartFile image) throws IOException {
        // 파일 이름과 확장자 추출
        String originalFilename = image.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // 확장자 추출
        String s3FileName = UUID.randomUUID().toString().substring(0, 10) + "_" + originalFilename;

        // 파일 크기 및 메타데이터 설정
        long fileSize = image.getSize();  // 파일 크기
        InputStream inputStream = image.getInputStream();
        byte[] bytes = IOUtils.toByteArray(inputStream);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentType("image/" + extension);
        metadata.setContentLength(bytes.length);

        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);

        try {
            // S3에 파일 업로드
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, s3FileName, byteArrayInputStream, metadata)
                    .withCannedAcl(CannedAccessControlList.PublicRead);

            amazonS3.putObject(putObjectRequest);
        } catch (AmazonS3Exception e) {
            throw new AmazonS3Exception("이미지를 S3 버킷에 업로드할 수 없습니다.", e);
        } finally {
            byteArrayInputStream.close();
            inputStream.close();
        }

        // 업로드된 파일의 URL 생성
        String fileUrl = amazonS3.getUrl(bucketName, s3FileName).toString();

        // UploadFileResponse 객체 반환 (파일 이름, 확장자, 파일 크기, URL)
        return new UploadFileResponse(
                originalFilename,  // 실제 파일 이름
                extension,         // 파일 확장자
                fileSize,          // 파일 크기
                fileUrl            // S3에 저장된 파일 URL
        );
    }

    public void deleteImageFromS3(String imageAddress) {
        String key = getKeyFromImageAddress(imageAddress);
        try {
            amazonS3.deleteObject(new DeleteObjectRequest(bucketName, key));
        } catch (AmazonS3Exception e) {
            throw new AmazonS3Exception("이미지를 S3 버킷으로부터 삭제할 수 없습니다.", e);
        }
    }

    public String getKeyFromImageAddress(String imageAddress) {
        try {
            URL url = new URL(imageAddress);
            String decodingKey = URLDecoder.decode(url.getPath(), "UTF-8");
            return decodingKey.substring(1);
        } catch (MalformedURLException | UnsupportedEncodingException e) {
            throw new AmazonS3Exception("유효하지 않은 이미지 URL입니다.", e);
        }
    }
}
