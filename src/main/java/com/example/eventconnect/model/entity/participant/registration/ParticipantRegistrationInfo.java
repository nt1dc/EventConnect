package com.example.eventconnect.model.entity.participant.registration;

import com.example.eventconnect.model.entity.Event;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor

@Entity
public class ParticipantRegistrationInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @OneToMany
    private List<ParticipantRegistrationParameter> participantRegistrationParameter;
}
