package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Version;

@Getter
@Setter
@Entity
@Table(name = "laundries_state")
public class LaundryStateEntity {

    private int id;
    @Version
    private int version;
    private long queueWaitingTime;
}
