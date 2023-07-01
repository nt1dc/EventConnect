package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.ParticipantEventParamDto;
import com.example.eventconnect.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final EventService eventService;

    public ParticipantController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/events/{eventId}/registration")
    public List<EventRegistrationParamsResponse> getRegistrationParameters(@PathVariable Long eventId) {
        return eventService.getRegistrationParameters(eventId);
    }

    @PostMapping("/events/{eventId}/registration")
    public void postRegistrationParameters(
            @PathVariable Long eventId,
            @RequestBody List<ParticipantEventParamDto> participantEventParamDtos,
            Principal principal) {
        eventService.registerParticipant(eventId, participantEventParamDtos, principal.getName());
    }
}