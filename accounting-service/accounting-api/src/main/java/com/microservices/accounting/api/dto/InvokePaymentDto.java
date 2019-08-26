package com.microservices.accounting.api.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@Setter
@ToString
public class InvokePaymentDto {
    @NotNull
    @Valid
    private UserDto user;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal amountOfMoney;
}
