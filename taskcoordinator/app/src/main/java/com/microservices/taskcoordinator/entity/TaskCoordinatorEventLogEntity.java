package com.microservices.taskcoordinator.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

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
@ToString
@Table(name = "taskcoordinator_events")
public class TaskCoordinatorEventLogEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private long createdTime = System.currentTimeMillis();

    @Enumerated(value = EnumType.STRING)
    private EventStatus eventStatus = EventStatus.PENDING;

    @Enumerated(value = EnumType.STRING)
    private EventType eventType;

    @ToString.Exclude
    private byte[] message;

    public TaskCoordinatorEventLogEntity(EventType eventType, byte[] message) {
        this.eventType = eventType;
        this.message = message;
    }
}
