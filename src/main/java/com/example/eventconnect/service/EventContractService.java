package com.example.eventconnect.service;

import com.example.eventconnect.model.entity.Event;
import com.example.eventconnect.model.entity.contract.EventContractStatus;

public interface EventContractService {
    void createContract(Event event, EventContractStatus eventContractStatus);

    void updateContractStatus(Long contractId, EventContractStatus eventContractStatus);
}
