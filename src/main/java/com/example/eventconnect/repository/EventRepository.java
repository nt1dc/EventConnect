package com.example.eventconnect.repository;

import com.example.eventconnect.model.dto.EventResponse;
import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.EventStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    List<Event> findAllByEventStatusIn(Collection<EventStatus> eventStatus);
}