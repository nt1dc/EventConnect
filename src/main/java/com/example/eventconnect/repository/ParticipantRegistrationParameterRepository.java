package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.participant.ParticipantRegistrationParams;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParticipantRegistrationParameterRepository extends JpaRepository<ParticipantRegistrationParams, Long> {
}