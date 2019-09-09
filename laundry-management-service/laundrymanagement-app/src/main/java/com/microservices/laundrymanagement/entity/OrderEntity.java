package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private int id;

    private long submittedTime = System.currentTimeMillis();

    private long estimatedTime;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.QUEUED;

    private int laundryId;

    private int bucket;
}
