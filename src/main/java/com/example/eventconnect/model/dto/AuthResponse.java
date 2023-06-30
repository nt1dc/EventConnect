package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Set;

@Data
@AllArgsConstructor

public class AuthResponse {
    private String login;
    private String accessToken;
    private String refreshToken;
    private Set<Role> roles;
}
