package com.example.eventconnect.service.admin;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.service.event.EventService;
import com.example.eventconnect.service.event.contract.EventContractService;
import org.hibernate.Hibernate;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AdminServiceImpl implements AdminService {
    private final EventContractService eventContractService;
    private final EventService eventService;
    private ModelMapper modelMapper = new ModelMapper();

    public AdminServiceImpl(EventContractService eventContractService, EventService eventService) {
        this.eventContractService = eventContractService;
        this.eventService = eventService;
    }

    @Override
    public void updateContractStatus(Long contractId, EventContractStatus eventContractStatus) {
        EventContract eventContract = eventContractService.getEventContract(contractId);
        eventContract.setStatus(eventContractStatus);
        if (eventContractStatus == EventContractStatus.APPROVED) {
            Event event = eventContract.getEvent();
            event.setEventStatus(EventStatus.APPROVED);
            eventService.saveEvent(event);
        }
        eventContractService.saveContract(eventContract);
    }

    @Override
    public List<EventContractResponse> getAllContracts() {
        return eventContractService.getAll().stream().map((element) -> modelMapper.map(element, EventContractResponse.class)).collect(Collectors.toList());
    }
}
