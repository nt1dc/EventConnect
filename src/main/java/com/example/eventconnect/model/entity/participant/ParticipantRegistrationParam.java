package com.example.eventconnect.model.entity.participant;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor


@Entity
public class ParticipantRegistrationParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private Participant participant;
    private String userAnswer;

    @ManyToOne
    @JoinColumn(name = "event_registration_param_id")
    private EventRegistrationParam eventRegistrationParam;
}
