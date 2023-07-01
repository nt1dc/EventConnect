package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class EventResponse {
    private Long id;
    private String name;
    private String description;
    private EventStatus eventStatus;
    private UserInfoResponse eventAdmin;
}
