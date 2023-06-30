package com.example.eventconnect.model.entity.participant.registration;

import com.example.eventconnect.model.entity.Event;
import jakarta.persistence.*;

@Entity
public class EventRegistrationParameter {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    private Event event;
    private String name;
    private String description;
    private Boolean checkRequire;
}
