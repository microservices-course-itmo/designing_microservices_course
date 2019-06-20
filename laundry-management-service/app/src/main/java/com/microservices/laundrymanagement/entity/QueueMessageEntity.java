package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "queue_messages")
public class QueueMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
    private int orderId;
    @ManyToOne
    @JoinColumn(name = "landry_state_id")
    private LaundryStateEntity laundryState;
    @Enumerated(value = EnumType.STRING)
    private MessageStatus messageStatus;

    public QueueMessageEntity(int orderId, LaundryStateEntity laundryState) {
        this.laundryState = laundryState;
        this.orderId = orderId;
        this.messageStatus = MessageStatus.PENDING;
    }
}
