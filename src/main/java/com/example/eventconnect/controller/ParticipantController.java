package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.EventRegistrationParametersResponse;
import com.example.eventconnect.model.dto.EventRegistrationParamsDto;
import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import com.example.eventconnect.service.EventService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/participant")
public class ParticipantController {
    private final EventService eventService;

    public ParticipantController(EventService eventService) {
        this.eventService = eventService;
    }

    @GetMapping("/event/{eventId}/registration")
    public List<EventRegistrationParametersResponse> getRegistrationParameters(@PathVariable Long eventId){
        return eventService.getRegistrationParameters(eventId);
    }
    @PostMapping("/event/{eventId}/registration")
    public void postRegistrationParameters(@PathVariable Long eventId){

    }
}