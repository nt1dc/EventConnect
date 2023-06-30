package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.participant.registration.EventRegistrationParameter;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationParameterRepository extends JpaRepository<EventRegistrationParameter, Long> {
}