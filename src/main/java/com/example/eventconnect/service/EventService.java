package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventRegistrationParamsResponse;
import com.example.eventconnect.model.dto.EventResponse;

import java.util.List;

public interface EventService {
    List<EventResponse> getAvailableEvents();

    List<EventRegistrationParamsResponse> getRegistrationParameters(Long eventId);
}
