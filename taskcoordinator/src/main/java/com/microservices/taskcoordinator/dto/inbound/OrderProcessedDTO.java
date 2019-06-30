package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderEntity;
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

    public OrderProcessedDTO(OrderEntity orderEntity, LaundryStateEntity laundryStateEntity) {
        this.orderId = orderEntity.getId();
        this.laundryState = new LaundryStateDTO(laundryStateEntity);
        this.completionTime = orderEntity.getCompletionTime();
    }
}
