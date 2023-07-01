package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.model.dto.ParticipantEventPRegistrationParamDto;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.EventStatus;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.participant.EventParticipant;
import com.example.eventconnect.model.entity.participant.EventRegistrationParams;
import com.example.eventconnect.model.entity.participant.ParticipantRegistrationParams;
import com.example.eventconnect.model.entity.participant.ParticipantStatus;
import com.example.eventconnect.repository.EventParticipantRepository;
import com.example.eventconnect.repository.EventRepository;
import com.example.eventconnect.service.auth.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EventParticipantRepository eventParticipantRepository;
    private final UserService userService;

    public EventServiceImpl(EventRepository eventRepository, EventParticipantRepository eventParticipantRepository, UserService userService) {
        this.eventRepository = eventRepository;
        this.eventParticipantRepository = eventParticipantRepository;
        this.userService = userService;
    }

    @Override
    public List<EventResponse> getAvailableEvents() {
        return eventRepository.findAllByEventStatusIn(Set.of(EventStatus.APPROVED))
                .stream()
                .map((element) -> modelMapper.map(element, EventResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new)
                .getEventRegistrationParams()
                .stream()
                .map((element) -> modelMapper.map(element, EventRegistrationParamsResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void registerParticipant(Long eventId, List<ParticipantEventPRegistrationParamDto> participantEventPRegistrationParamsDtoDto, String login) {
        Event event = eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        User participantUser = userService.getUserByLogin(login);
        if (eventParticipantRepository.existsByEventAndAndUser(event, participantUser))
            throw new RuntimeException("already registered");

        if (event.getEventRegistrationParams().size() < participantEventPRegistrationParamsDtoDto.size())
            throw new RuntimeException("invalid params count");

        boolean allParamsPresent = event.getEventRegistrationParams().stream()
                .map(EventRegistrationParams::getId)
                .collect(Collectors.toSet())
                .containsAll(
                        participantEventPRegistrationParamsDtoDto
                                .stream()
                                .map(ParticipantEventPRegistrationParamDto::getParamId)
                                .collect(Collectors.toSet())
                );
        if (!allParamsPresent) throw new RuntimeException("not all params present");
        EventParticipant participant = EventParticipant.builder()
                .user(participantUser)
                .event(event)
                .participantStatus(ParticipantStatus.CREATED)
                .build();

        Map<Long, EventRegistrationParams> registrationParamsMap = event.getEventRegistrationParams().stream()
                .collect(Collectors.toMap(EventRegistrationParams::getId, v -> v));

        Set<ParticipantRegistrationParams> participantRegistrationParams = participantEventPRegistrationParamsDtoDto.stream()
                .map(userParam -> ParticipantRegistrationParams.builder()
                        .participant(participant)
                        .userAnswer(userParam.getUserAnswer())
                        .eventRegistrationParams(registrationParamsMap.get(userParam.getParamId()))
                        .build()
                ).collect(Collectors.toSet());
        participant.setRegistrationParams(participantRegistrationParams);
        eventParticipantRepository.save(participant);
    }
}
