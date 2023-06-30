package com.example.eventconnect.model.entity.participant.registration;

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
    private EventParticipant participant;
    private String userAnswer;
}
