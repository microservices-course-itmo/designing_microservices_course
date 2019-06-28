package com.microservices.taskcoordinator.dto.outbound;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;
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
    int laundryId;
    int bucket;
    LaundryStateDTO laundryState;

    /*public OrderSubmissionDTO(OrderEntity orderEntity) {
        this.orderId = orderEntity.getId();
        this.laundryState = new LaundryStateDTO(orderEntity.getLaundryId(), orderEntity.get)

    }*/
}
