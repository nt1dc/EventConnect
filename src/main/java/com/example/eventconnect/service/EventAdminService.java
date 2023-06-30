package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventDto;

public interface EventAdminService {
    void createEvent(EventDto eventDto, String userLogin);
}
