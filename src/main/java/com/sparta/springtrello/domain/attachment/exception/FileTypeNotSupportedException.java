package com.sparta.springtrello.domain.attachment.exception;

public class FileTypeNotSupportedException extends RuntimeException {
    public FileTypeNotSupportedException(String message) {
        super(message);
    }
}
