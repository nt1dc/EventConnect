package com.example.eventconnect.exception;

public class ParticipantNotFoundException extends RuntimeException {
    public ParticipantNotFoundException(Long participantId) {
        super("participant with  id:" + participantId + "not found");
    }
}
