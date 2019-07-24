package com.microservices.accounting.service;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.dto.PaymentStatus;
import com.microservices.accounting.dto.RevertPaymentDto;
import com.microservices.accounting.entity.PaymentEntity;
import com.microservices.accounting.repository.PaymentRepository;
import com.microservices.accounting.temporarydtos.CardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {
    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto) {
        Objects.requireNonNull(invokePaymentDto);

        CardInfo cardInfo = invokePaymentDto.getUser().getCardInfo();
        PaymentStatus paymentStatus = cardInfo.equals(CardInfo.VALID) && isStatusApproved()
                ? PaymentStatus.ACCEPTED
                : PaymentStatus.DENIED;

        PaymentEntity save = paymentRepository.save(new PaymentEntity(invokePaymentDto, paymentStatus));
        return new PaymentDetailsDto(save);
    }

    @Override
    public PaymentDetailsDto revertPayment(RevertPaymentDto revertPaymentDto) {
        Objects.requireNonNull(revertPaymentDto);

        PaymentEntity paymentToRevert = paymentRepository.findById(revertPaymentDto.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("No payment with id" + revertPaymentDto.getPaymentId()));

        paymentToRevert.setPaymentStatus(PaymentStatus.REVERTED);
        PaymentEntity updatedPaymentEntity = paymentRepository.save(paymentToRevert);
        return new PaymentDetailsDto(updatedPaymentEntity);
    }

    /**
     * Define is payment approved. 1 of 10 valid cards are denied
     */
    private boolean isStatusApproved() { //TODO shine2: rename and add to re
        return new Random().nextInt(10) > 1;
    }
}
