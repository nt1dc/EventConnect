package com.example.eventconnect.model.dto.event.registration;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantAnwerWithQuestionDto {
    private Long paramId;
    private String paramName;
    private String paramDescription;
    private String participantAnswer;
}
