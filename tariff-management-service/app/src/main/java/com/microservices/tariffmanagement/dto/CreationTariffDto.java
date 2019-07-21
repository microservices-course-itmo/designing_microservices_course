package com.microservices.tariffmanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class CreationTariffDto {
    @NotNull
    private String name;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;

    @NotNull
    private Long washingTime;
}
