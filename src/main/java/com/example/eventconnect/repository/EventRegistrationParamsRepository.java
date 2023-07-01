package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.participant.EventRegistrationParam;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRegistrationParamsRepository extends JpaRepository<EventRegistrationParam, Long> {
}