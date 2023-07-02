package com.example.eventconnect.controller;


import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.service.event.EventService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/events")
@RestController
public class EventController {
    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/")
    public List<EventInfoResponse> getAvailableEvents() {
        return eventService.getAvailableEvents();
    }
}

