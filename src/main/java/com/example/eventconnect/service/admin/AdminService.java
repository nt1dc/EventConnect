package com.example.eventconnect.service.admin;

import com.example.eventconnect.model.dto.contract.EventContractResponse;
import com.example.eventconnect.model.entity.contract.EventContractStatus;

import java.util.List;

public interface AdminService {
    void updateContractStatus(Long contractId, EventContractStatus eventContractStatus);

    List<EventContractResponse> getAllContracts();
}
