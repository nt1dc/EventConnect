package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventStatusIn(Collection<EventStatus> eventStatus);
    Optional<Event> findByIdAndEventStatus(Long id, EventStatus eventStatus);
}