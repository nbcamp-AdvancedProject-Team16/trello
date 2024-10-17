package com.sparta.springtrello.domain.common.exception;

import com.sparta.springtrello.domain.attachment.dto.ErrorResponse;
import com.sparta.springtrello.domain.attachment.exception.AttachmentNotFoundException;
import com.sparta.springtrello.domain.attachment.exception.FileTypeNotSupportedException;
import com.sparta.springtrello.domain.attachment.exception.InvalidRequestException;
import com.sparta.springtrello.domain.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MaxUploadSizeExceededException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ApiResponse<Void>> handleCustomException(CustomException e) {
    ApiResponse<Void> response = new ApiResponse<>(e.getStatus(), e.getMessage(), null);
    HttpStatus status = HttpStatus.valueOf(e.getStatus());
    return new ResponseEntity<>(response, status);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiResponse<Void>> handleGeneralException(Exception ex) {
    ApiResponse<Void> response = new ApiResponse<>(500, "Internal server error", null);
    return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @ExceptionHandler(MaxUploadSizeExceededException.class)
  public ResponseEntity<ErrorResponse> handleMaxUploadSizeExceededException(MaxUploadSizeExceededException ex) {
    return ResponseEntity.status(HttpStatus.PAYLOAD_TOO_LARGE)
            .body(new ErrorResponse(413, "파일 크기가 5MB를 초과합니다.", null));
  }

  @ExceptionHandler(FileTypeNotSupportedException.class)
  public ResponseEntity<ErrorResponse> handleFileTypeNotSupportedException(FileTypeNotSupportedException ex) {
    return ResponseEntity.badRequest()
            .body(new ErrorResponse(400, ex.getMessage(), null));
  }

  @ExceptionHandler(InvalidRequestException.class)
  public ResponseEntity<ErrorResponse> handleInvalidRequestException(InvalidRequestException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, ex.getMessage(), null));
  }

  @ExceptionHandler(AttachmentNotFoundException.class)
  public ResponseEntity<ErrorResponse> handleAttachmentNotFoundException(AttachmentNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new ErrorResponse(404, "해당 카드 ID에 대한 첨부파일이 없습니다.", null));
  }
}
