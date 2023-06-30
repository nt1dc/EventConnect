package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.Event;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventRepository extends JpaRepository<Event, Long> {
}