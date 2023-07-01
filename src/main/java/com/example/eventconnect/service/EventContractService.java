package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.contract.EventContractStatus;

import java.util.List;

public interface EventContractService {
    void createContract(Event event, EventContractStatus eventContractStatus);

    void updateContractStatus(Long contractId, EventContractStatus eventContractStatus);

    List<EventContractResponse> getAll();
}
