package com.example.eventconnect.model.dto.auth;

import com.example.eventconnect.model.entity.user.Role;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Set;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class AuthResponse {
    private String login;
    private String accessToken;
    private String refreshToken;
    private Set<Role> roles;
}
