package com.microservices.tariffmanagement.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "tariffs")
public class TariffEntity {
    @Id
    private long id;

    private String name;

    private double price;

    private long washingTimeInMillis;
}
