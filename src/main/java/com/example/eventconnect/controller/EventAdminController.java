package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.model.dto.ParticipantAnswersResponse;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.service.EventAdminService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

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

    @PutMapping("/events/{eventId}/participants/{participantId}")
    public void updateParticipantStatus(@PathVariable Long eventId,
                                        @PathVariable Long participantId,
                                        @RequestBody ParticipationStatus participationStatus,
                                        Principal principal) {
        eventAdminService.updateParticipantStatus(eventId, participantId, principal.getName(), participationStatus);
    }

    @GetMapping("/events/{eventId}/participants/answers")
    public List<ParticipantAnswersResponse> getParticipants(@PathVariable Long eventId, Principal principal) {
        return eventAdminService.getEventParticipantsAnswers(eventId, principal.getName());
    }

}
