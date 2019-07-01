package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "details")
public class OrderDetailEntity {

    @Id
    private int id;

    private int weight;

    private long duration;

    public OrderDetailEntity(OrderDetailDTO orderDetailDTO) {
        this.id = orderDetailDTO.getDetailId();
        this.weight = orderDetailDTO.getWeight();
        this.duration = orderDetailDTO.getDuration();
    }
}
