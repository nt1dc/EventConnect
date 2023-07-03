package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.dto.event.create.EventCreateRequest;
import com.example.eventconnect.model.dto.event.registration.ParticipantRegistrationResponse;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.service.event.admin.EventAdminService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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

    @GetMapping("/events")
    public List<EventInfoResponse> getMyEvents(Principal principal) {
        User user = (User) principal;
        return eventAdminService.getEvents(user.getLogin());
    }

    @PutMapping("/events/{eventId}/participants/{participantId}")
    public void updateParticipantStatus(@PathVariable Long eventId,
                                        @PathVariable Long participantId,
                                        @RequestBody ParticipationStatus participationStatus,
                                        Principal principal) {
        eventAdminService.updateParticipantStatus(eventId, participantId, principal.getName(), participationStatus);
    }

    @GetMapping("/events/{eventId}/participants/answers")
    public List<ParticipantRegistrationResponse> getParticipants(@PathVariable Long eventId, Principal principal) {
        return eventAdminService.getEventParticipantsAnswers(eventId, principal.getName());
    }

    @PutMapping("/contracts/{contractId}/sign")
    public void singContract(@PathVariable Long contractId, @AuthenticationPrincipal User principalUser) {
        eventAdminService.signContract(contractId, principalUser.getLogin());
    }

    @GetMapping("/contracts")
    public List<EventContractResponse> getContracts(@AuthenticationPrincipal User principalUser) {
        return eventAdminService.getContracts(principalUser.getId());
    }
}
