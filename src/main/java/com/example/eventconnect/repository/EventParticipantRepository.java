package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.participant.Participant;
import com.example.eventconnect.model.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventParticipantRepository extends JpaRepository<Participant, Long> {
    boolean existsByEventAndAndUser(Event event, User user);
}