package com.microservices.accounting.service;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.dto.PaymentStatus;
import com.microservices.accounting.temporarydtos.CardInfo;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto) {
        CardInfo cardInfo = invokePaymentDto.getUser().getCardInfo();
        Integer userId = invokePaymentDto.getUser().getId();
        BigDecimal amountOfMoney = invokePaymentDto.getAmountOfMoney();
        switch (cardInfo) {
            case VALID:
                return isStatusApproved()
                        ? new PaymentDetailsDto(userId, amountOfMoney, PaymentStatus.ACCEPTED)
                        : new PaymentDetailsDto(userId, amountOfMoney, PaymentStatus.DENIED);
            case INVALID:
                return new PaymentDetailsDto(userId, amountOfMoney, PaymentStatus.DENIED);
            default:
                throw new AssertionError("Unknown card status: " + cardInfo);
        }
    }

    /**
     * Define is payment approved. 1 of 10 valid cards are denied
     */
    private boolean isStatusApproved() {
        return new Random().nextInt(10) > 1;
    }
}
