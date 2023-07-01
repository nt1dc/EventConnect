package com.example.eventconnect.model.dto.auth;

import lombok.Data;

@Data
public class AuthRequest {
    private String login;
    private String password;

}
