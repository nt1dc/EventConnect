package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.model.dto.ParticipantAnswersResponse;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;

import java.util.List;

public interface EventAdminService {
    void createEvent(EventCreateRequest eventCreateRequest, String userLogin);

    void updateParticipantStatus(Long eventId, Long participantId, String eventAdminLogin, ParticipationStatus id);

    List<ParticipantAnswersResponse> getEventParticipantsAnswers(Long eventId, String evenAdminName);
}
