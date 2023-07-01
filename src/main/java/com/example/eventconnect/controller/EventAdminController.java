package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.service.EventAdminService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/event-admin")
public class EventAdminController {
    private final EventAdminService eventAdminService;

    public EventAdminController(EventAdminService eventAdminService) {
        this.eventAdminService = eventAdminService;
    }

    @PostMapping("/event")
    public void createEvent(@RequestBody EventCreateRequest eventCreateRequest, Principal principal) {
        eventAdminService.createEvent(eventCreateRequest, principal.getName());
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
