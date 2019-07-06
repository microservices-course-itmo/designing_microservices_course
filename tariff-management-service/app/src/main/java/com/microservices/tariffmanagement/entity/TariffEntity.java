package com.microservices.tariffmanagement.entity;

import com.microservices.tariffmanagement.dto.TariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "tariffs")
@NoArgsConstructor
@Getter
public class TariffEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private BigDecimal price;

    //in millis
    private long washingTime;

    public TariffEntity(TariffDto tariffDto) {
        this.name = tariffDto.getName();
        this.price = BigDecimal.valueOf(tariffDto.getPrice());
        this.washingTime = tariffDto.getWashingTime();
    }
}
