package com.microservices.laundrymanagement.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@Table(name = "laundries")
public class LaundryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "laundries_generator")
    @SequenceGenerator(name = "laundries_generator", sequenceName = "laundries_id_seq")
    private int id;

    private String name;

    public LaundryEntity(String name) {
        this.name = name;
    }
}
