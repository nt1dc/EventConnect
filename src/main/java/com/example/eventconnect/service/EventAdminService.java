package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.EventCreateRequest;

public interface EventAdminService {
    void createEvent(EventCreateRequest eventCreateRequest, String userLogin);
}
