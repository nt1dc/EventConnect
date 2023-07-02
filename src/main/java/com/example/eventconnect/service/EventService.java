package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.event.registration.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;
import com.example.eventconnect.model.entity.event.Event;

import java.util.List;

public interface EventService {
    List<EventInfoResponse> getAvailableEvents();

    List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId);

    void registerParticipant(Long eventId, List<ParticipantEventParamDto> participantEventParamDtos, String login);

    void saveEvent(Event event);
    Event getEventById(Long eventId);
}
