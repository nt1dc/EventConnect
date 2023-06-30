package com.example.eventconnect.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event-admin")
public class EventAdminController {
    @GetMapping
    public String ping(){
        return "pong";
    }
}
