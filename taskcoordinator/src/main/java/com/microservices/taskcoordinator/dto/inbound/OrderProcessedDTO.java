package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class OrderProcessedDTO {

    int orderId;
    LaundryStateDTO laundryState;
    long completionTime;
}
