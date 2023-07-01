package com.example.eventconnect.model.entity;

import com.example.eventconnect.model.entity.participant.EventRegistrationParams;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity

public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String description;
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "event_admin_id")
    private User eventAdmin;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EventRegistrationParams> eventRegistrationParams = new HashSet<>();
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;
}
