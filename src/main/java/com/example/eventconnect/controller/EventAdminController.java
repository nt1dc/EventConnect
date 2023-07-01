package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
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

    @PostMapping("/events")
    public void createEvent(@RequestBody EventCreateRequest eventCreateRequest, Principal principal) {
        eventAdminService.createEvent(eventCreateRequest, principal.getName());
    }

    @PutMapping("/events/{eventId}/participant/{participantId}")
    public void updateParticipantStatus(@PathVariable Long eventId,
                                        @PathVariable Long participantId,
                                        @RequestBody ParticipationStatus participationStatus,
                                        Principal principal) {
        eventAdminService.updateParticipantStatus(eventId, participantId, principal.getName(), participationStatus);
    }

    @GetMapping("/ping")
    public String ping() {
        return "pong";
    }
}
