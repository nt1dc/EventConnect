package com.example.eventconnect.exception;

public class EventContractNotFoundException extends RuntimeException {
    public EventContractNotFoundException(Long eventId) {
        super("event with id:" + eventId + " not found");
    }
}
