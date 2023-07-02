package com.example.eventconnect.model.dto.event.create;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter

public class EventRegistrationParamsDto {
    private String name;
    private String description;
    private Boolean checkRequire;
}