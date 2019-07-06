package com.microservices.tariffmanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TariffDto {
    private String name;

    private double price;

    private long washingTime;
}
