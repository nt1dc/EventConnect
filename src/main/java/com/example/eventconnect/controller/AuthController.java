package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.auth.AuthRequest;
import com.example.eventconnect.model.dto.auth.AuthResponse;
import com.example.eventconnect.model.dto.auth.RegisterRequest;
import com.example.eventconnect.service.auth.AuthService;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest authRequest) {
        return authService.login(authRequest);
    }

    @PostMapping("/register")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }

    @PostMapping("/token/refresh")
    public String refresh(@RequestHeader(name = "Authorization") String token) {
        return authService.generateRefreshTokenByAccess(token);
    }
}
