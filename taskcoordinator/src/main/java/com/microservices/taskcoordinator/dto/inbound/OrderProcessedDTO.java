package com.microservices.taskcoordinator.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class OrderProcessedDTO {

    Integer orderId;

    InboundLaundryStateDTO laundryState;

    Long completionTime;
}
