package com.sparta.springtrello.domain.notification.exception;

public class NotificationDisabledException extends RuntimeException {
    public NotificationDisabledException(String message) {
        super(message);
    }
}