package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private int id;

    private long submittedTime;

    private long estimatedTime;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private int laundryId;

    private int bucket;
}
