package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDTO {

    private int detailId;

    private int weight;

    private long duration;

    private int orderId;

    public OrderDetailDTO(OrderDetailEntity orderDetailEntity) {
        this.detailId = orderDetailEntity.getId();
        this.weight = orderDetailEntity.getWeight();
        this.duration = orderDetailEntity.getDuration();
        this.orderId = orderDetailEntity.getOrderId();
    }
}