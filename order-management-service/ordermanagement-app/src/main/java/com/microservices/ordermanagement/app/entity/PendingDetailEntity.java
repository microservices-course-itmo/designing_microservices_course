package com.microservices.ordermanagement.app.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pending_details")
public class PendingDetailEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int id;

    private int weight;

    private long createdTime = System.currentTimeMillis();

    public PendingDetailEntity(int weight) {
        this.weight = weight;
    }
}
