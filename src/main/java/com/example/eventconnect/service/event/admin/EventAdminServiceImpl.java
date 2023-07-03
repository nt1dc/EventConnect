package com.example.eventconnect.service.event.admin;

import com.example.eventconnect.exception.ContractSignException;
import com.example.eventconnect.exception.ParticipantNotFoundException;
import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.dto.event.create.EventCreateRequest;
import com.example.eventconnect.model.dto.event.registration.ParticipantAnwerWithQuestionDto;
import com.example.eventconnect.model.dto.event.registration.ParticipantRegistrationResponse;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.model.entity.participant.EventRegistrationParam;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.participant.ParticipationStatus;
import com.example.eventconnect.model.entity.user.User;
import com.example.eventconnect.repository.EventParticipantRepository;
import com.example.eventconnect.service.auth.UserService;
import com.example.eventconnect.service.event.EventService;
import com.example.eventconnect.service.event.contract.EventContractService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.eventconnect.model.entity.contract.EventContractStatus.CREATED;
import static com.example.eventconnect.model.entity.contract.EventContractStatus.SIGNED;

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
        Set<EventRegistrationParam> eventRegistrationParams = eventCreateRequest.getEventRegistrationParams().stream()
                .map(element -> {
                    EventRegistrationParam params = modelMapper.map(element, EventRegistrationParam.class);
                    params.setEvent(event);
                    return params;
                })
                .collect(Collectors.toSet());
        event.setName(eventCreateRequest.getName());
        event.setEventAdmin(user);
        event.setEventRegistrationParams(eventRegistrationParams);
        event.setDescription(eventCreateRequest.getDescription());
        event.setEventStatus(EventStatus.CREATED);
        eventService.saveEvent(event);
        eventContractService.createContract(event, EventContractStatus.CREATED);
    }

    @Override
    public void updateParticipantStatus(Long eventId, Long participantId, String eventAdminLogin, ParticipationStatus participationStatus) {
        User eventAdmin = userService.getUserByLogin(eventAdminLogin);
        Event event = eventService.getEventById(eventId);
        boolean equals = event.getEventAdmin().equals(eventAdmin);
        if (!equals) throw new AccessDeniedException("not owner");
        Participant participant = eventParticipantRepository.findById(participantId).orElseThrow(() -> new ParticipantNotFoundException(participantId));
        participant.setParticipationStatus(participationStatus);
        eventParticipantRepository.save(participant);
    }

    @Override
    public List<ParticipantRegistrationResponse> getEventParticipantsAnswers(Long eventId, String evenAdminName) {
        Event event = eventService.getEventById(eventId);
        return event.getParticipants().stream().map(
                participant -> new ParticipantRegistrationResponse(
                        participant.getId(),
                        participant.getUser().getLogin(),
                        participant.getRegistrationParams()
                                .stream().map(participantRegistrationParams ->
                                        new ParticipantAnwerWithQuestionDto(
                                                participantRegistrationParams.getEventRegistrationParam().getId(),
                                                participantRegistrationParams.getEventRegistrationParam().getName(),
                                                participantRegistrationParams.getEventRegistrationParam().getDescription(),
                                                participantRegistrationParams.getUserAnswer()
                                        )).collect(Collectors.toSet()))).toList();

    }

    @Override
    public void signContract(Long contractId, String eventAdminId) {
        User eventAdmin = userService.getUserByLogin(eventAdminId);
        EventContract eventContract = eventContractService.getEventContract(contractId);
        if (!Objects.equals(eventAdmin.getId(), eventContract.getEvent().getEventAdmin().getId())) {
            throw new AccessDeniedException("this user is not admin event");
        }
        if (eventContract.getStatus() != CREATED) {
            throw new ContractSignException("error sign contract");
        }
        eventContract.setStatus(SIGNED);
        eventContractService.saveContract(eventContract);
    }

    @Override
    public List<EventContractResponse> getContracts(Long eventAdminID) {
        return eventContractService.getAll().stream()
                .filter(contract -> Objects.equals(contract.getEvent().getEventAdmin().getId(), eventAdminID))
                .map((element) -> modelMapper.map(element, EventContractResponse.class)).toList();
    }

    @Override
    public List<EventInfoResponse> getEvents(String eventAdminId) {
        return eventService.getAllEvents().stream()
                .filter(event -> Objects.equals(event.getEventAdmin().getLogin(), eventAdminId))
                .map((element) -> modelMapper.map(element, EventInfoResponse.class)).toList();
    }
}
