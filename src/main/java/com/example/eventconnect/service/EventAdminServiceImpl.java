package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventCreateRequest;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import com.example.eventconnect.repository.EventRepository;
import com.example.eventconnect.service.auth.UserService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventAdminServiceImpl implements EventAdminService {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EventRepository eventRepository;
    private final EventContractService eventContractService;

    public EventAdminServiceImpl(UserService userService, EventRepository eventRepository, EventContractService eventContractService) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventContractService = eventContractService;
    }


    @Override
    public void createEvent(EventCreateRequest eventCreateRequest, String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        Event event = new Event();
        List<EventRegistrationParams> eventRegistrationParams = eventCreateRequest.getEventRegistrationParams().stream()
                .map(element -> {
                    EventRegistrationParams params = modelMapper.map(element, EventRegistrationParams.class);
                    params.setEvent(event);
                    return params;
                })
                .collect(Collectors.toList());
        event.setName(eventCreateRequest.getName());
        event.setEventAdmin(user);
        event.setEventRegistrationParams(eventRegistrationParams);
        event.setDescription(eventCreateRequest.getDescription());
        eventRepository.save(event);
        eventContractService.createContract(event, EventContractStatus.CREATED);
    }
}
