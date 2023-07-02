package com.example.eventconnect.service.event;

import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.entity.event.Event;

import java.util.List;

public interface EventService {
    List<EventInfoResponse> getAvailableEvents();

    void saveEvent(Event event);
    Event getEventById(Long eventId);
    Event getApprovedEventById(Long eventId);
}
