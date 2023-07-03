package com.example.eventconnect.model.entity.event;

import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.user.User;
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
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true,fetch = FetchType.EAGER)
    private Set<EventRegistrationParam> eventRegistrationParams = new HashSet<>();
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;
    @OneToMany(mappedBy = "event", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Participant> participants;
}
