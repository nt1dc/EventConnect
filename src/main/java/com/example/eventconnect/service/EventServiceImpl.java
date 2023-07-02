package com.example.eventconnect.service;

import com.example.eventconnect.exception.EventNotFoundException;
import com.example.eventconnect.model.dto.event.registration.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.participant.EventRegistrationParam;
import com.example.eventconnect.model.entity.participant.ParticipantRegistrationParam;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
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
    public List<EventInfoResponse> getAvailableEvents() {
        return eventRepository.findAllByEventStatusIn(Set.of(EventStatus.APPROVED))
                .stream()
                .map((element) -> modelMapper.map(element, EventInfoResponse.class))
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
        if (!allParamsPresent) {
            throw new IllegalArgumentException("Not all parameters are present");
        }

        boolean needsEventAdminCheck = event.getEventRegistrationParams().stream().anyMatch(EventRegistrationParam::getCheckRequire);

        ParticipationStatus participationStatus = needsEventAdminCheck ? ParticipationStatus.CREATED : ParticipationStatus.APPROVED;


        Participant participant = createParticipant(participantUser, event, participationStatus, participantEventPRegistrationParamsDtoDto);

        eventParticipantRepository.save(participant);
    }

    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("element with id:" + eventId + " not found"));
    }

    private boolean checkIfAllParamsPresent(Event event, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto) {
        Set<Long> requiredParamIds = event.getEventRegistrationParams().stream()
                .map(EventRegistrationParam::getId)
                .collect(Collectors.toSet());

        Set<Long> participantParamIds = participantEventPRegistrationParamsDtoDto.stream()
                .map(ParticipantEventParamDto::getParamId)
                .collect(Collectors.toSet());

        return requiredParamIds.containsAll(participantParamIds);
    }

    private Participant createParticipant(User participantUser, Event event, ParticipationStatus participationStatus, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto) {
        Participant participant = new Participant();
        Map<Long, EventRegistrationParam> registrationParamsMap = event.getEventRegistrationParams().stream()
                .collect(Collectors.toMap(EventRegistrationParam::getId, Function.identity()));

        Set<ParticipantRegistrationParam> participantRegistrationParams = participantEventPRegistrationParamsDtoDto.stream()
                .map(userParam -> createParticipantRegistrationParam(userParam, registrationParamsMap, participant))
                .collect(Collectors.toSet());
        participant.setUser(participantUser);
        participant.setEvent(event);
        participant.setParticipationStatus(participationStatus);
        participant.setRegistrationParams(participantRegistrationParams);
        return participant;
    }

    private ParticipantRegistrationParam createParticipantRegistrationParam(ParticipantEventParamDto userParam, Map<Long, EventRegistrationParam> registrationParamsMap, Participant participant) {
        EventRegistrationParam eventRegistrationParam = registrationParamsMap.get(userParam.getParamId());

        return ParticipantRegistrationParam.builder()
                .userAnswer(userParam.getUserAnswer())
                .eventRegistrationParam(eventRegistrationParam)
                .participant(participant)
                .build();
    }

}
