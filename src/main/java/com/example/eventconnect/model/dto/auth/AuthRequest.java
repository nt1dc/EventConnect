package com.example.eventconnect.model.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class AuthRequest {
    @Email
    private String login;
    @Size(min = 6)
    private String password;
}
