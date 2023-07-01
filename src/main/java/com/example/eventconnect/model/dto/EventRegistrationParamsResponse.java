package com.example.eventconnect.model.dto;

import com.example.eventconnect.model.entity.participant.EventRegistrationParam;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO for {@link EventRegistrationParam}
 */
@Getter
@Setter
@NoArgsConstructor
public class EventRegistrationParamsResponse {
    Long id;
    String name;
    String description;
}