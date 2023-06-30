package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.AuthRequest;
import com.example.eventconnect.model.dto.AuthResponse;
import com.example.eventconnect.model.dto.RegisterRequest;
import com.example.eventconnect.service.auth.AuthService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @PostMapping("/register1")
    public void register(@RequestBody RegisterRequest registerRequest) {
        authService.register(registerRequest);
    }
}
