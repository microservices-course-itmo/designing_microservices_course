package com.microservices.accounting.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PaymentDetailsDto {
    @NotNull
    private Integer paymentId;

    @NotNull
    private String username;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private PaymentStatus paymentStatus;
}
