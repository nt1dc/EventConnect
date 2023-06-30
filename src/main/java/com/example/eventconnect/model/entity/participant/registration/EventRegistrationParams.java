package com.example.eventconnect.model.entity.participant.registration;

import com.example.eventconnect.model.entity.Event;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor


@Entity
public class EventRegistrationParams {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    private String name;
    private String description;
    private Boolean checkRequire;
    @ManyToMany
    private List<ParticipantRegistrationParams> participantRegistrationParams;
}
