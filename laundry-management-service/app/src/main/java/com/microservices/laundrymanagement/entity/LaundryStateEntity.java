package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "laundries_state")
public class LaundryStateEntity {

    @Id
    private int id;

    @Version
    private int version;

    @Column
    private long queueWaitingTime;

    public LaundryStateEntity(int id) {
        this.id = id;
    }
}
