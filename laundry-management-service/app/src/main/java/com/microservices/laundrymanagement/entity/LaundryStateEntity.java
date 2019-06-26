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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laundries_state_generator")
    @SequenceGenerator(name = "laundries_state_generator", sequenceName = "laundries_state_id_seq")
    private int id;

    @Version
    private int version;

    @Column
    private long queueWaitingTime;

    private String name;

    public LaundryStateEntity(String name) {
        this.name = name;
    }
}
