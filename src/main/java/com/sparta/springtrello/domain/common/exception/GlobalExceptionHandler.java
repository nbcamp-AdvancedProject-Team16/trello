package com.sparta.springtrello.domain.common.exception;

import com.sparta.springtrello.domain.common.response.ApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
}
