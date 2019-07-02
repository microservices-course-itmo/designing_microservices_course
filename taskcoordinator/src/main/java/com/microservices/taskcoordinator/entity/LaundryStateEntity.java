package com.microservices.taskcoordinator.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

@Getter
@Setter
@Entity
@Table(name = "laundries_state")
public class LaundryStateEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private Integer version;

    private Long queueWaitingTime;

    private Long reservedTime;

    public long getCompletionTimePrediction() {
        return this.getQueueWaitingTime() + this.getReservedTime();
    }
}
