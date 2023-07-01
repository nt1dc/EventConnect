package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.model.dto.ParticipantEventParamDto;
import com.example.eventconnect.model.entity.Event;

import java.util.List;

public interface EventService {
    List<EventResponse> getAvailableEvents();

    List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId);

    void registerParticipant(Long eventId, List<ParticipantEventParamDto> participantEventParamDtos, String login);

    void saveEvent(Event event);
    Event getEventById(Long eventId);
}
