package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.service.EventContractService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final EventContractService eventContractService;

    public AdminController(EventContractService eventContractService) {
        this.eventContractService = eventContractService;
    }

    @PutMapping("/events/contracts/{contractId}/status")
    public void updateContractStatus(@PathVariable Long contractId, @RequestBody EventContractStatus eventContractStatus) {
        eventContractService.updateContractStatus(contractId, eventContractStatus);
    }
    @GetMapping("/events/contracts")
    private List<EventContractResponse> getAllContracts(){
        return eventContractService.getAll();
    }
}
