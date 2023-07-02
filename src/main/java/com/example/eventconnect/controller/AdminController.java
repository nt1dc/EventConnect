package com.example.eventconnect.controller;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import com.example.eventconnect.service.admin.AdminService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PutMapping("/events/contracts/{contractId}/status")
    public void updateContractStatus(@PathVariable Long contractId, @RequestBody EventContractStatus eventContractStatus) {
        adminService.updateContractStatus(contractId, eventContractStatus);
    }
    @GetMapping("/events/contracts")
    private List<EventContractResponse> getAllContracts(){
        return adminService.getAllContracts();
    }
}
