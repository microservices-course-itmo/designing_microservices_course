package com.microservices.accounting.dto;

import com.microservices.accounting.entity.PaymentEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.Objects;

@NoArgsConstructor
@Getter
@Setter
@ToString
@AllArgsConstructor
public class PaymentDetailsDto {
    @NotNull //through that dto will be used by other modules, validation annotations will be processed in them
    private Integer paymentId;

    @NotNull
    private String userName;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal amount;

    @NotNull
    private PaymentStatus paymentStatus;

    public PaymentDetailsDto(PaymentEntity paymentEntity) {
        Objects.requireNonNull(paymentEntity);

        this.paymentId = paymentEntity.getPaymentId();
        this.userName = paymentEntity.getUserName();
        this.amount = paymentEntity.getAmount();
        this.paymentStatus = paymentEntity.getPaymentStatus();
    }
}
