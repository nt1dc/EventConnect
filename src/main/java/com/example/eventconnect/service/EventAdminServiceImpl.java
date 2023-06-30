package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventDto;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import com.example.eventconnect.repository.EventRepository;
import com.example.eventconnect.service.auth.UserService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventAdminServiceImpl implements EventAdminService {
    private final UserService userService;
    private final ModelMapper modelMapper = new ModelMapper();
    private final EventRepository eventRepository;

    public EventAdminServiceImpl(UserService userService, EventRepository eventRepository) {
        this.userService = userService;
        this.eventRepository = eventRepository;
    }


    @Override
    public void createEvent(EventDto eventDto, String userLogin) {
        User user = userService.getUserByLogin(userLogin);
        List<EventRegistrationParams> eventRegistrationParams = eventDto.getEventRegistrationParams().stream()
                .map((element) -> modelMapper.map(element, EventRegistrationParams.class))
                .toList();

        Event event = Event.builder()
                .name(eventDto.getName())
                .eventAdmin(user)
                .eventRegistrationParams(eventRegistrationParams)
                .description(eventDto.getDescription()).build();
        eventRepository.save(event);
    }
}
