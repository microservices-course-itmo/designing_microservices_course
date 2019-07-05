package com.microservices.taskcoordinator.dto.outbound;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class OrderSubmissionDTO {

    private int orderId;

    private int laundryId;

    private int bucket;

    private List<OrderDetailDTO> details;

    public OrderSubmissionDTO(OrderEntity orderEntity) {
        this.orderId = orderEntity.getId();
        this.laundryId = orderEntity.getLaundryId();
        this.bucket = orderEntity.getBucket();
        this.details = orderEntity.getDetails() == null
                ? null
                : orderEntity.getDetails().stream()
                    .map(OrderDetailDTO::new)
                    .collect(Collectors.toList());
    }
}
