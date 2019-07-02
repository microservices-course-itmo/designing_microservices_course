package com.microservices.ordermanagement.app.entity;

import com.microservices.ordermanagement.app.dto.TariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
public class OrderDetailEntity {
    @Id
    int id;

    private int orderId;

    private int weight;

    private Integer tariffId;

    private BigDecimal price;

    public OrderDetailEntity(int id, int orderId, int weight) {
        this.id = id;
        this.orderId = orderId;
        this.weight = weight;
    }

    public void addTariffInformation(TariffDto tariffDto) {
        this.setTariffId(tariffDto.getId());
        this.setPrice(tariffDto.getPrice());
    }
}
