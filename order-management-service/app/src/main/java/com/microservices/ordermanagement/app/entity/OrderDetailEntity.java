package com.microservices.ordermanagement.app.entity;

import com.microservices.ordermanagement.api.dto.TariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "order_details")
class OrderDetailEntity {
    @Id
    int id;

    private int orderId;

    private int weight;

    private Integer tariffId;

    @NotNull
    private BigDecimal price = BigDecimal.ZERO;

    OrderDetailEntity(int id, int orderId, int weight) {
        this.id = id;
        this.orderId = orderId;
        this.weight = weight;
    }

    void addTariffInformation(TariffDto tariffDto) {
        Objects.requireNonNull(tariffDto);

        this.setTariffId(Objects.requireNonNull(tariffDto.getId()));
        this.setPrice(Objects.requireNonNull(tariffDto.getPrice()));
    }
}
