package com.example.eventconnect.service;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.model.entity.event.EventStatus;
import com.example.eventconnect.repository.EventContractRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventContractServiceImpl implements EventContractService {
    private final EventContractRepository eventContractRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public EventContractServiceImpl(EventContractRepository eventContractRepository
                                    ) {
        this.eventContractRepository = eventContractRepository;

    }

    @Override
    public void createContract(Event event, EventContractStatus eventContractStatus) {
        EventContract eventContract = EventContract.builder()
                .event(event)
                .status(eventContractStatus).build();
        eventContractRepository.save(eventContract);
    }

    @Override
    public void updateContractStatus(Long contractId, EventContractStatus eventContractStatus) {
        EventContract eventContract = eventContractRepository.findById(contractId).orElseThrow(EntityNotFoundException::new);
        eventContract.setStatus(eventContractStatus);
        eventContract.getEvent().setEventStatus(EventStatus.valueOf(eventContractStatus.name()));
        eventContractRepository.save(eventContract);
    }

    @Override
    public List<EventContractResponse> getAll() {
        return eventContractRepository.findAll().stream().map((element) -> modelMapper.map(element, EventContractResponse.class)).collect(Collectors.toList());
    }
}
