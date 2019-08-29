package com.microservices.tariffmanagement.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class TariffDto {

    private int id;

    private String name;

    private BigDecimal price;

    private long washingTime;
}
