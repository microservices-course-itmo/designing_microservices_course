package com.microservices.accounting.entity;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;


//TODO shine2: fix flyway
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "payments")
public class PaymentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int paymentId;

    private BigDecimal amount;

    private String userName;

    @Enumerated(value = EnumType.STRING)
    private PaymentStatus paymentStatus;

    public PaymentEntity(InvokePaymentDto invokePaymentDto, PaymentStatus status) {
        Objects.requireNonNull(invokePaymentDto);

        this.amount = invokePaymentDto.getAmountOfMoney();
        this.userName = invokePaymentDto.getUser().getLogin();
        this.paymentStatus = Objects.requireNonNull(status);
//        this.paymentStatus = status;
    }
}
