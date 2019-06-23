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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laundries_state_generator")
    @SequenceGenerator(name="laundries_state_generator", sequenceName = "laundries_state_id_seq")
    private int id;

    @Version
    private int version;

    @Column
    private long queueWaitingTime;

    @Column
    private long reservedTime;
}
