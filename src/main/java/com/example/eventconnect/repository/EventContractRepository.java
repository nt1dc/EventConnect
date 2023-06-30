package com.example.eventconnect.repository;

import com.example.eventconnect.model.entity.contract.EventContract;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EventContractRepository extends JpaRepository<EventContract, Long> {
}