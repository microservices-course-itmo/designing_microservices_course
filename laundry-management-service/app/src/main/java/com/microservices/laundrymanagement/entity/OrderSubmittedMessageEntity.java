package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "order_submitted_messages")
public class OrderSubmittedMessageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int orderId;

    @Enumerated(value = EnumType.STRING)
    private MessageStatus messageStatus;

    private int laundryId;

    private int laundryStateVersion;

    private long queueWaitingTime;

    public OrderSubmittedMessageEntity(int orderId, LaundryStateEntity laundryState) {
        this.orderId = orderId;
        this.messageStatus = MessageStatus.PENDING;
        this.laundryId = laundryState.getId();
        this.laundryStateVersion = laundryState.getVersion();
        this.queueWaitingTime = laundryState.getQueueWaitingTime();
    }
}
