package com.example.eventconnect.model.entity;

import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParameter;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
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
    @OneToMany
    private List<EventRegistrationParameter> eventRegistrationParameters;
    @ManyToMany
    private List<User> participants;

}
