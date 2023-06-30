package com.example.eventconnect.model.entity.participant.registration;

import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import jakarta.persistence.*;

import java.util.List;

@Entity
public class EventParticipant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @Enumerated(value = EnumType.STRING)
    private ParticipantStatus participantStatus;

}
