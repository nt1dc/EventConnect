package com.example.eventconnect.service.event;

import com.example.eventconnect.exception.EventNotFoundException;
import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventServiceImpl implements EventService {
    private final EventRepository eventRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public EventServiceImpl(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    public List<EventInfoResponse> getEventsInfo() {
        return eventRepository.findAll()
                .stream()
                .map((element) -> modelMapper.map(element, EventInfoResponse.class))
                .collect(Collectors.toList());
    }


    @Override
    public void saveEvent(Event event) {
        eventRepository.save(event);
    }

    @Override
    public Event getEventById(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(() -> new EventNotFoundException("element with id:" + eventId + " not found"));
    }


    @Override
    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }


}
