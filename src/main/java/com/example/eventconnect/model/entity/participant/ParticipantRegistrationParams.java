package com.example.eventconnect.model.entity.participant;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor


@Entity
public class ParticipantRegistrationParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "participant_id")
    private EventParticipant participant;
    private String userAnswer;

    @ManyToOne
    @JoinColumn(name = "event_registration_params_id")
    private EventRegistrationParams eventRegistrationParams;
}
