package com.example.eventconnect.service.auth;

import com.example.eventconnect.model.dto.auth.AuthRequest;
import com.example.eventconnect.model.dto.auth.AuthResponse;
import com.example.eventconnect.model.dto.auth.RegisterRequest;


public interface AuthService {
    void register(RegisterRequest registerRequest);

    AuthResponse login(AuthRequest authRequest);

    String generateRefreshTokenByAccess(String token);
}
