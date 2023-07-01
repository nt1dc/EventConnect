package com.example.eventconnect.model.entity.contract;

import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor

@Entity
public class EventContract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @Enumerated(EnumType.STRING)
    private EventContractStatus status;
}
