package com.microservices.tariffmanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TariffDto {
    private String name;

    private double price;

    private long washingTime;
}
