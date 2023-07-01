package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.User;
import com.example.eventconnect.model.entity.participant.EventParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<EventParticipant, Long> {
    boolean existsByEventAndAndUser(Event event, User user);
}