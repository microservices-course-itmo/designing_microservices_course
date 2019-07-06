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
public class OrderDetailDto {

    private Integer detailId;

    private Integer weight;

    private Long duration;

    private Integer orderId;

    public OrderDetailDto(OrderDetailEntity orderDetailEntity) {
        this.detailId = orderDetailEntity.getId();
        this.weight = orderDetailEntity.getWeight();
        this.duration = orderDetailEntity.getDuration();
        this.orderId = orderDetailEntity.getOrderId();
    }
}