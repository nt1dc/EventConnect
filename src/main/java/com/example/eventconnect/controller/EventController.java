package com.example.eventconnect.controller;


import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.service.EventService;
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
    public List<EventResponse> getAvailableEvents() {
        return eventService.getAvailableEvents();
    }
}

