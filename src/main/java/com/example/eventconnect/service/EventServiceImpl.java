package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.model.dto.ParticipantEventParamDto;
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
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@Transactional
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
    public void registerParticipant(Long eventId, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto, String login) {
        Event event = eventRepository.findById(eventId).orElseThrow(EntityNotFoundException::new);
        User participantUser = userService.getUserByLogin(login);

        if (eventParticipantRepository.existsByEventAndAndUser(event, participantUser)) {
            throw new IllegalStateException("Participant already registered");
        }

        if (event.getEventRegistrationParams().size() < participantEventPRegistrationParamsDtoDto.size()) {
            throw new IllegalArgumentException("Invalid parameter count");
        }

        boolean allParamsPresent = checkIfAllParamsPresent(event, participantEventPRegistrationParamsDtoDto);
        boolean needsEventAdminCheck = event.getEventRegistrationParams().stream().anyMatch(EventRegistrationParams::getCheckRequire);

        ParticipantStatus participantStatus = needsEventAdminCheck ? ParticipantStatus.CREATED : ParticipantStatus.APPROVED;

        if (!allParamsPresent) {
            throw new IllegalArgumentException("Not all parameters are present");
        }

        EventParticipant participant = createParticipant(participantUser, event, participantStatus, participantEventPRegistrationParamsDtoDto);
        eventParticipantRepository.save(participant);
    }

    private boolean checkIfAllParamsPresent(Event event, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto) {
        Set<Long> requiredParamIds = event.getEventRegistrationParams().stream()
                .map(EventRegistrationParams::getId)
                .collect(Collectors.toSet());

        Set<Long> participantParamIds = participantEventPRegistrationParamsDtoDto.stream()
                .map(ParticipantEventParamDto::getParamId)
                .collect(Collectors.toSet());

        return requiredParamIds.containsAll(participantParamIds);
    }

    private EventParticipant createParticipant(User participantUser, Event event, ParticipantStatus participantStatus, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto) {
        Map<Long, EventRegistrationParams> registrationParamsMap = event.getEventRegistrationParams().stream()
                .collect(Collectors.toMap(EventRegistrationParams::getId, Function.identity()));

        Set<ParticipantRegistrationParams> participantRegistrationParams = participantEventPRegistrationParamsDtoDto.stream()
                .map(userParam -> createParticipantRegistrationParam(userParam, registrationParamsMap))
                .collect(Collectors.toSet());

        return EventParticipant.builder()
                .user(participantUser)
                .event(event)
                .participantStatus(participantStatus)
                .registrationParams(participantRegistrationParams)
                .build();
    }

    private ParticipantRegistrationParams createParticipantRegistrationParam(ParticipantEventParamDto userParam, Map<Long, EventRegistrationParams> registrationParamsMap) {
        EventRegistrationParams eventRegistrationParams = registrationParamsMap.get(userParam.getParamId());

        return ParticipantRegistrationParams.builder()
                .userAnswer(userParam.getUserAnswer())
                .eventRegistrationParams(eventRegistrationParams)
                .build();
    }

}
