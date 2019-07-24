package com.microservices.accounting.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PaymentDetailsDto {
    private Integer userId;
    private PaymentStatus paymentStatus;
}
