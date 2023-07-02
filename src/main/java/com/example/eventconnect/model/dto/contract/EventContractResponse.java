package com.example.eventconnect.model.dto.contract;

import com.example.eventconnect.model.dto.event.EventInfoResponse;
import com.example.eventconnect.model.entity.contract.EventContractStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class EventContractResponse {
    private Long id;
    private EventContractStatus status;
    private EventInfoResponse event;
}
