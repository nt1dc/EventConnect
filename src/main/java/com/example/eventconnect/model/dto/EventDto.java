package com.example.eventconnect.model.dto;


import lombok.*;

import java.util.List;
@Getter
@Setter

@NoArgsConstructor
@AllArgsConstructor
public class EventDto {
    private String name;
    private String description;
    private List<EventRegistrationParamsDto> eventRegistrationParams;
}
