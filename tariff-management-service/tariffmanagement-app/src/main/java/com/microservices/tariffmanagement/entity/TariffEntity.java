package com.microservices.tariffmanagement.entity;

import com.microservices.tariffmanagement.api.dto.TariffDto;
import com.microservices.tariffmanagement.dto.CreationTariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
@NoArgsConstructor
@Getter
@ToString
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private BigDecimal price;

    //in millis
    private long washingTime;

    public TariffEntity(@NonNull CreationTariffDto creationTariffDto) {
        this.name = creationTariffDto.getName();
        this.price = creationTariffDto.getPrice();
        this.washingTime = creationTariffDto.getWashingTime();
    }

    public TariffDto toTariffDto() {
        return new TariffDto(id, name, price, washingTime);
    }
}
