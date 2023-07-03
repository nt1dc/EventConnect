package com.example.eventconnect.exception;

public class EventNotApprovedException extends RuntimeException{
    public EventNotApprovedException(String message) {
        super(message);
    }
}
