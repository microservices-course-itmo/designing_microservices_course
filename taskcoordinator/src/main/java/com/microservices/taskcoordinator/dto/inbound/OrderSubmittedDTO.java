package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class OrderSubmittedDTO {

    private int orderId;
    private LaundryStateDTO laundryState;
}
