package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.participant.EventRegistrationParams;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.repository.EventParticipantRepository;
import com.example.eventconnect.service.auth.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventAdminServiceImpl implements EventAdminService {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EventContractService eventContractService;
    private final EventService eventService;
    private final EventParticipantRepository eventParticipantRepository;

    public EventAdminServiceImpl(UserService userService, EventContractService eventContractService, EventService eventService, EventParticipantRepository eventParticipantRepository) {
        this.userService = userService;
        this.eventContractService = eventContractService;
        this.eventService = eventService;
        this.eventParticipantRepository = eventParticipantRepository;
    }


    @Override
    public void createEvent(EventCreateRequest eventCreateRequest, String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        Event event = new Event();
        Set<EventRegistrationParams> eventRegistrationParams = eventCreateRequest.getEventRegistrationParams().stream()
                .map(element -> {
                    EventRegistrationParams params = modelMapper.map(element, EventRegistrationParams.class);
                    params.setEvent(event);
                    return params;
                })
                .collect(Collectors.toSet());
        event.setName(eventCreateRequest.getName());
        event.setEventAdmin(user);
        event.setEventRegistrationParams(eventRegistrationParams);
        event.setDescription(eventCreateRequest.getDescription());
        eventService.saveEvent(event);
        eventContractService.createContract(event, EventContractStatus.CREATED);
    }

    @Override
    public void updateParticipantStatus(Long eventId, Long participantId, String eventAdminLogin, ParticipationStatus participationStatus) {
        User eventAdmin = userService.getUserByLogin(eventAdminLogin);
        Event event = eventService.getEventById(eventId);
        boolean equals = event.getEventAdmin().equals(eventAdmin);
        if (!equals) throw new RuntimeException("not owner");
        Participant participant = eventParticipantRepository.findByEventAndAndUser(event, eventAdmin);
        participant.setParticipationStatus(participationStatus);
        eventParticipantRepository.save(participant);
    }
}
