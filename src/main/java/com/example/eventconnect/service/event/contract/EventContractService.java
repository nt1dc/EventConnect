package com.example.eventconnect.service.event.contract;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;

import java.util.List;

public interface EventContractService {
    void createContract(Event event, EventContractStatus eventContractStatus);


    List<EventContractResponse> getAll();

    EventContract getEventContract(Long eventContractID);

    void saveContract(EventContract eventContract);
}
