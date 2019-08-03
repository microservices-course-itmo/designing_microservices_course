package com.microservices.tariffmanagement.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public class TariffDto {

    private int id;

    private String name;

    private BigDecimal price;

    private long washingTime;
}
