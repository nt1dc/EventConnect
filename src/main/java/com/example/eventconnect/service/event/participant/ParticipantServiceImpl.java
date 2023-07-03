package com.example.eventconnect.service.event.participant;

import com.example.eventconnect.exception.EventNotApprovedException;
import com.example.eventconnect.model.dto.event.registration.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.event.registration.ParticipantEventParamDto;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.model.entity.participant.EventRegistrationParam;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.participant.ParticipantRegistrationParam;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.EventParticipantRepository;
import com.example.eventconnect.repository.UserRepository;
import com.example.eventconnect.service.event.EventService;
import com.example.eventconnect.service.auth.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
public class ParticipantServiceImpl implements ParticipantService {
    private final EventService eventService;
    private final EventParticipantRepository eventParticipantRepository;
    private final ModelMapper modelMapper = new ModelMapper();
    private final UserService userService;

    public ParticipantServiceImpl(EventService eventService, EventParticipantRepository eventParticipantRepository,
                                  UserRepository userRepository, UserService userService1) {
        this.eventService = eventService;
        this.eventParticipantRepository = eventParticipantRepository;

        this.userService = userService1;
    }

    @Override
    public List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId) {
        return eventService.getEventById(eventId)
                .getEventRegistrationParams()
                .stream()
                .map((element) -> modelMapper.map(element, EventRegistrationParamsResponse.class))
                .collect(Collectors.toList());
    }

    @Override
    public void registerParticipant(Long eventId, List<ParticipantEventParamDto> participantEventPRegistrationParamsDtoDto, String userLogin) {
        User participantUser = userService.getUserByLogin(userLogin);
        Event event = eventService.getEventById(eventId);
        if (event.getEventStatus() != EventStatus.APPROVED) throw new EventNotApprovedException("event not approved");
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
