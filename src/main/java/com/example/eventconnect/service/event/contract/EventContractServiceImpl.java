package com.example.eventconnect.service.event.contract;

import com.example.eventconnect.exception.EventContractNotFoundException;
import com.example.eventconnect.model.entity.contract.EventContract;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.model.entity.event.Event;
import com.example.eventconnect.repository.EventContractRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
    public List<EventContract> getAll() {
        return eventContractRepository.findAll();
    }

    @Override
    public EventContract getEventContract(Long eventContractID) {
        return eventContractRepository.findById(eventContractID).orElseThrow(()-> new EventContractNotFoundException(eventContractID));
    }

    @Override
    public void saveContract(EventContract eventContract) {
        eventContractRepository.save(eventContract);
    }
}
