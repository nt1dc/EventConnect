package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link EventRegistrationParams}
 */
@Getter
@Setter
@NoArgsConstructor
public class EventRegistrationParamsResponse {
    Long id;
    String name;
    String description;
}