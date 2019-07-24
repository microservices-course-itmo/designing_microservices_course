package com.microservices.accounting.service;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.dto.PaymentStatus;
import com.microservices.accounting.temporarydtos.CardInfo;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Override
    public PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto) {
        CardInfo cardInfo = invokePaymentDto.getUser().getCardInfo();
        Integer userId = invokePaymentDto.getUser().getId();
        switch (cardInfo) {
            case VALID:
                return isStatusApproved()
                        ? new PaymentDetailsDto(userId, PaymentStatus.ACCEPTED)
                        : new PaymentDetailsDto(userId, PaymentStatus.DENIED);
            case INVALID:
                return new PaymentDetailsDto(userId, PaymentStatus.DENIED);
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
