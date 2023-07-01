package com.example.eventconnect.model.entity;

import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
    private List<EventRegistrationParams> eventRegistrationParams = new ArrayList<>();
    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus;


}
