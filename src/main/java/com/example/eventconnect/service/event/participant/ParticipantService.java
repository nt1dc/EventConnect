package com.example.eventconnect.service.event.participant;

import com.example.eventconnect.model.dto.event.registration.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;

import java.util.List;

public interface ParticipantService {
    List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId);

    void registerParticipant(Long eventId, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto, String userLogin);
}
