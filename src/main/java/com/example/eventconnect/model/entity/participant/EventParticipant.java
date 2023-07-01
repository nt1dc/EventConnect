package com.example.eventconnect.model.entity.participant;

import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

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
    @OneToMany(mappedBy = "participant", cascade = CascadeType.ALL,orphanRemoval = true)
    private Set<ParticipantRegistrationParams> registrationParams = new HashSet<>();

}
