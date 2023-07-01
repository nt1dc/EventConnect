package com.example.eventconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    public HealthController() {
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
