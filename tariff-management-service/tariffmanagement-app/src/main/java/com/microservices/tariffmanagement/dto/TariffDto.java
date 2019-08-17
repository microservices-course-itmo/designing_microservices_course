package com.microservices.tariffmanagement.dto;

import com.microservices.tariffmanagement.entity.TariffEntity;
import lombok.Getter;
import lombok.NonNull;

import java.math.BigDecimal;

@Getter
public class TariffDto {
    private int id;

    private String name;

    private BigDecimal price;

    private long washingTime;

    public TariffDto(@NonNull TariffEntity tariffEntity) {
        this.id = tariffEntity.getId();
        this.name = tariffEntity.getName();
        this.price = tariffEntity.getPrice();
        this.washingTime = tariffEntity.getWashingTime();
    }
}
