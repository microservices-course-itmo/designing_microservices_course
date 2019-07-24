package com.microservices.accounting.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
public class RevertPaymentDto {
    @NotNull
    private String userName;

    @NotNull
    private Integer paymentId;
}
