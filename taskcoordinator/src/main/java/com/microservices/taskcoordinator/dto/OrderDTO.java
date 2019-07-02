package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.stream.Collectors;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDTO {

    private int id;

    private int laundryId;

    private int bucket;

    private long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private long estimatedTime;

    private long completionTime;

    private List<OrderDetailDTO> details;

    public OrderDTO(OrderEntity orderEntity) {
        this.id = orderEntity.getId();
        this.laundryId = orderEntity.getLaundryId();
        this.bucket = orderEntity.getBucket();
        this.duration = orderEntity.getDuration();
        this.status = orderEntity.getStatus();
        this.estimatedTime = orderEntity.getEstimatedTime();
        this.completionTime = orderEntity.getCompletionTime();

        //TODO NPE case
        this.details = orderEntity.getDetails().stream()
                .map(OrderDetailDTO::new)
                .collect(Collectors.toList());
    }
}
