package com.microservices.accounting.dto;

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
    @NotNull //through that dto will be used by other modules, validation annotations will be processed in them
    private Integer userId;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private PaymentStatus paymentStatus;
}
