package com.example.eventconnect.service.event.admin;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.dto.event.create.EventCreateRequest;
import com.example.eventconnect.model.dto.event.registration.ParticipantRegistrationResponse;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;

import java.util.List;

public interface EventAdminService {
    void createEvent(EventCreateRequest eventCreateRequest, String userLogin);

    void updateParticipantStatus(Long eventId, Long participantId, String eventAdminLogin, ParticipationStatus id);

    List<ParticipantRegistrationResponse> getEventParticipantsAnswers(Long eventId, String evenAdminName);
    void signContract(Long contractId, String eventAdminLogin);

    List<EventContractResponse> getContracts(Long eventAdminID);
}
