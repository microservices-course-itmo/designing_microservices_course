package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.OrderDetailDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "details")
public class OrderDetailEntity {

    @Id
    private int  id;

    private int weight;

    private long duration;

    private int orderId;

    public OrderDetailEntity(OrderDetailDto orderDetailDTO) {
        Objects.requireNonNull(orderDetailDTO);

        this.id = orderDetailDTO.getId();
        this.weight = orderDetailDTO.getWeight();
        this.duration = orderDetailDTO.getDuration();
        this.orderId = orderDetailDTO.getOrderId();
    }
}
