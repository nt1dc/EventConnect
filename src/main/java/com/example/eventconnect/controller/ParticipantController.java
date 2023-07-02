package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.event.registration.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.service.event.participant.ParticipantService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final ParticipantService participantService;

    public ParticipantController(ParticipantService participantService) {
        this.participantService = participantService;
    }

    @GetMapping("/events/{eventId}/registration")
    public List<EventRegistrationParamsResponse> getRegistrationParameters(@PathVariable Long eventId) {
        return participantService.getRegistrationParameters(eventId);
    }

    @PostMapping("/events/{eventId}/registration")
    public void registerOnEvent(@PathVariable Long eventId, @RequestBody List<ParticipantEventParamDto> participantEventParamDtos, @AuthenticationPrincipal User principalUser) {
        participantService.registerParticipant(eventId, participantEventParamDtos, principalUser.getUsername());
    }
}