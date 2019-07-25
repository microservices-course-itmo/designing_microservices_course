package com.microservices.accounting.service;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.dto.PaymentStatus;
import com.microservices.accounting.dto.RevertPaymentDto;
import com.microservices.accounting.entity.PaymentEntity;
import com.microservices.accounting.repository.PaymentRepository;
import com.microservices.accounting.temporaryclasses.CardInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.UnavailableException;
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
        PaymentStatus paymentStatus = (cardInfo == CardInfo.VALID && isRequestApproved())
                ? PaymentStatus.ACCEPTED
                : PaymentStatus.DENIED;

        PaymentEntity save = paymentRepository.save(new PaymentEntity(invokePaymentDto, paymentStatus));
        return new PaymentDetailsDto(save);
    }

    @Override
    @Transactional
    public PaymentDetailsDto revertPayment(RevertPaymentDto revertPaymentDto) throws UnavailableException {
        Objects.requireNonNull(revertPaymentDto);

        PaymentEntity paymentToRevert = paymentRepository.findById(revertPaymentDto.getPaymentId())
                .orElseThrow(() -> new IllegalArgumentException("No payment with id " + revertPaymentDto.getPaymentId()));

        switch (paymentToRevert.getPaymentStatus()) {
            case REVERTED:
                return new PaymentDetailsDto(paymentToRevert);
            case DENIED:
                throw new IllegalArgumentException("Denied payment cannot be reverted");
            case ACCEPTED:
                if (isRequestApproved()) {
                    paymentToRevert.setPaymentStatus(PaymentStatus.REVERTED);
                    PaymentEntity updatedPaymentEntity = paymentRepository.save(paymentToRevert);
                    return new PaymentDetailsDto(updatedPaymentEntity);
                } else {
                    throw new UnavailableException("Payment reversion cannot be processed now. Try later");
                }
            default:
                throw new AssertionError("Unknown payment status");
        }
    }

    /**
     * A random boolean generator. Is a mock for real request processing (for example, bank validation).
     * <br/>
     * 1 of 10 requests are denied
     */
    private boolean isRequestApproved() {
        return new Random().nextInt(10) > 1;
    }
}
