package com.microservices.taskcoordinator.dto.outbound;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class OrderSubmissionDTO {

    int orderId;
    LaundryStateDTO laundryState;
}
