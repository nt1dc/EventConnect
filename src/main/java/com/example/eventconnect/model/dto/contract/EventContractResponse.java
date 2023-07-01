package com.example.eventconnect.model.dto.contract;

import com.example.eventconnect.model.dto.EventResponse;
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
    private EventResponse event;
    private EventContractStatus status;
}
