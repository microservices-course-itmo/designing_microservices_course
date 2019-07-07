package com.microservices.tariffmanagement.entity;

import com.microservices.tariffmanagement.dto.TariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
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

    public TariffEntity(TariffDto tariffDto) {
        this.name = tariffDto.getName();
        this.price = tariffDto.getPrice();
        this.washingTime = tariffDto.getWashingTime();
    }
}
