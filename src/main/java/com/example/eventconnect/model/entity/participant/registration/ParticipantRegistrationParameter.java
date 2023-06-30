package com.example.eventconnect.model.entity.participant.registration;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class ParticipantRegistrationParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userAnswer;
    @ManyToOne
    @JoinColumn(name = "event_registration_parameter_id")
    private EventRegistrationParameter eventRegistrationParameter;
}
