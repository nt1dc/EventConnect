package com.example.eventconnect.service.auth;

import com.example.eventconnect.model.dto.AuthRequest;
import com.example.eventconnect.model.dto.AuthResponse;
import com.example.eventconnect.model.dto.RegisterRequest;


public interface AuthService {
    void register(RegisterRequest registerRequest);

    AuthResponse login(AuthRequest authRequest);

}
