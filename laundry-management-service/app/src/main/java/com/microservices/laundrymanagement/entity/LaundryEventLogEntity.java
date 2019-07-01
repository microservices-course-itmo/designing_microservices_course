package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
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
@NoArgsConstructor
@Entity
@Table(name = "laundry_events")
public class LaundryEventLogEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long createdTime = System.currentTimeMillis();

    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus = EventStatus.PENDING;

    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    private byte[] message;

    public LaundryEventLogEntity(EventType eventType, byte[] message) {
        this.eventType = eventType;
        this.message = message;
    }
}
