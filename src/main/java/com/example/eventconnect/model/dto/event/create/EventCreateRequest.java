package com.example.eventconnect.model.dto.event.create;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class EventCreateRequest {
    private String name;
    private String description;
    private List<EventRegistrationParamsDto> eventRegistrationParams;
}
