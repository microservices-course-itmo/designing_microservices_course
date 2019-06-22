package com.microservices.laundrymanagement.entity;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@Entity
public class OrderCompletedMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderId;

    private long orderExecutionTime;

    @Enumerated(value = EnumType.STRING)
    private MessageStatus messageStatus;

    private int laundryId;

    private int laundryStateVersion;

    private long queueWaitingTime;

    public OrderCompletedMessageEntity(OrderEntity order, LaundryStateEntity laundryState) {
        this.orderId = order.getId();
        this.orderExecutionTime = System.currentTimeMillis() - order.getSubmittedTime();
        this.messageStatus = MessageStatus.PENDING;
        this.laundryId = laundryState.getId();
        this.laundryStateVersion = laundryState.getVersion();
        this.queueWaitingTime = laundryState.getQueueWaitingTime();
    }
}
