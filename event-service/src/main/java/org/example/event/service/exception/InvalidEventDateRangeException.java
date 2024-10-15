package org.example.event.service.exception;

public class InvalidEventDateRangeException extends RuntimeException {
    public InvalidEventDateRangeException(String message) {
        super(message);
    }
}
