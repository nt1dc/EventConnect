package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventDto;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.EventStatus;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import com.example.eventconnect.repository.EventRegistrationParamsRepository;
import com.example.eventconnect.repository.EventRepository;
import com.example.eventconnect.service.auth.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.eventconnect.model.entity.EventStatus.*;

@Service
public class EventAdminServiceImpl implements EventAdminService {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EventRepository eventRepository;
    private final EventRegistrationParamsRepository eventRegistrationParamsRepository;

    public EventAdminServiceImpl(UserService userService, EventRepository eventRepository, EventRegistrationParamsRepository eventRegistrationParamsRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
        this.eventRegistrationParamsRepository = eventRegistrationParamsRepository;
    }


    @Override
    public void createEvent(EventDto eventDto, String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        Event event = new Event();
        List<EventRegistrationParams> eventRegistrationParams = eventDto.getEventRegistrationParams().stream()
                .map(element -> {
                    EventRegistrationParams params = modelMapper.map(element, EventRegistrationParams.class);
                    params.setEvent(event);
                    return params;
                })
                .collect(Collectors.toList());
        event.setName(eventDto.getName());
        event.setEventAdmin(user);
        event.setEventRegistrationParams(eventRegistrationParams);
        event.setDescription(eventDto.getDescription());
        event.setEventStatus(CREATED);
        eventRepository.save(event);
    }
}
