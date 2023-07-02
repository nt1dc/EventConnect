package com.example.eventconnect.model.dto.event;

import com.example.eventconnect.model.dto.UserInfoResponse;
import com.example.eventconnect.model.entity.event.EventStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor

public class EventInfoResponse {
    private Long id;
    private String name;
    private String description;
    private EventStatus eventStatus;
    private UserInfoResponse eventAdmin;
}
