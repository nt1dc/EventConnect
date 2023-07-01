package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.participant.EventRegistrationParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRegistrationInfoRepository extends JpaRepository<EventRegistrationParams, Long> {
}