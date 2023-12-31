package com.example.eventconnect.model.dto.event.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class ParticipantRegistrationResponse {
    private Long participantId;
    private String participantLogin;
    private Set<ParticipantAnwerWithQuestionDto> anwerWithQuestionDtoSet;
}
