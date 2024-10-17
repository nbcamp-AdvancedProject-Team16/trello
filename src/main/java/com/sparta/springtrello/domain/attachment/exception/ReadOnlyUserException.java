package com.sparta.springtrello.domain.attachment.exception;

public class ReadOnlyUserException extends RuntimeException {
    public ReadOnlyUserException(String message) {
        super(message);
    }
}