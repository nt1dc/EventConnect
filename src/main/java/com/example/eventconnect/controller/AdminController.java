package com.example.eventconnect.controller;

import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.service.EventContractService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final EventContractService eventContractService;

    public AdminController(EventContractService eventContractService) {
        this.eventContractService = eventContractService;
    }

    @PutMapping("/event-contracts/{contractId}/status")
    public void updateEventContractStatus(@PathVariable Long contractId, @RequestBody EventContractStatus eventContractStatus) {
        eventContractService.updateContractStatus(contractId, eventContractStatus);
    }
}
