package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderDetailDto {

    private Integer id;

    private Integer weight;

    private Long duration;

    private Integer orderId;

    public OrderDetailDto(OrderDetailEntity orderDetailEntity) {
        this.id = orderDetailEntity.getId();
        this.weight = orderDetailEntity.getWeight();
        this.duration = orderDetailEntity.getDuration();
        this.orderId = orderDetailEntity.getOrderId();
    }
}