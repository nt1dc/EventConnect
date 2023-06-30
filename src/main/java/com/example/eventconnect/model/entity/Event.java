package com.example.eventconnect.model.entity;

import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParams;
import jakarta.persistence.*;
import lombok.*;

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
    @ManyToOne
    @JoinColumn(name = "event_admin_id")
    private User eventAdmin;
    @OneToMany(mappedBy = "event")
    private List<EventRegistrationParams> eventRegistrationParams;
}
