package com.example.eventconnect.exception;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String login) {
        super("user with login: " + login + " not found");
    }
}
