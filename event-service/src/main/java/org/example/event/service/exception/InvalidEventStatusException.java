package org.example.event.service.exception;

import org.example.event.service.model.EventStatus;

import java.util.Arrays;

public class InvalidEventStatusException extends RuntimeException {
    public InvalidEventStatusException(String status) {
        super("Invalid event status: " + status + ". Expected one of: " +
                Arrays.toString(EventStatus.values()));
    }
}
