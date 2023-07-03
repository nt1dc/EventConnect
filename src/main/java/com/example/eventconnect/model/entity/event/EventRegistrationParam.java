package com.example.eventconnect.model.entity.event;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor


@Entity
public class EventRegistrationParam {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne()
    @JoinColumn(name = "event_id")
    private Event event;
    private String name;
    private String description;
    private Boolean checkRequire;
}
