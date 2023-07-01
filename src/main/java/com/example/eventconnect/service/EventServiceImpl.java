package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.model.entity.EventStatus;
import com.example.eventconnect.repository.EventRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
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
}
