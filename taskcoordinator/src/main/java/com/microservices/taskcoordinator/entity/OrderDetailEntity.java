package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "details")
public class OrderDetailEntity {

    @Id
    private Integer id;

    private Integer weight;

    private Long duration;

    /*@Column(name = "order_id")*/
    private Integer orderId;

    public OrderDetailEntity(OrderDetailDTO orderDetailDTO) {
        this.id = orderDetailDTO.getDetailId();
        this.weight = orderDetailDTO.getWeight();
        this.duration = orderDetailDTO.getDuration();
        this.orderId = orderDetailDTO.getOrderId();
    }
}
