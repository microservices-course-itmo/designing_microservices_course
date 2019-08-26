package com.microservices.accounting.service;

import com.microservices.accounting.api.dto.CardInfo;
import com.microservices.accounting.api.dto.InvokePaymentDto;
import com.microservices.accounting.api.dto.PaymentDetailsDto;
import com.microservices.accounting.api.dto.PaymentStatus;
import com.microservices.accounting.entity.PaymentEntity;
import com.microservices.accounting.repository.PaymentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Random;

@Service
public class PaymentServiceImpl implements PaymentService {
    private final Logger logger = LoggerFactory.getLogger(PaymentServiceImpl.class);

    private PaymentRepository paymentRepository;

    @Autowired
    public PaymentServiceImpl(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    @Override
    public PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto) {
        Objects.requireNonNull(invokePaymentDto);

        logger.info("Invoking payment: {}...", invokePaymentDto);
        CardInfo cardInfo = invokePaymentDto.getUser().getCardInfo();
        PaymentStatus paymentStatus = (cardInfo == CardInfo.VALID && isRequestApproved())
                ? PaymentStatus.ACCEPTED
                : PaymentStatus.DENIED;

        PaymentEntity savedPayment = paymentRepository.save(new PaymentEntity(invokePaymentDto, paymentStatus));
        logger.info("Payment completed: {}", savedPayment);
        return savedPayment.toPaymentDetailsDto();
    }

    @Override
    @Transactional
    public PaymentDetailsDto revertPayment(int paymentId) {
        logger.info("Reverting payment with id: {}", paymentId);

        PaymentEntity paymentToRevert = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new IllegalArgumentException("No payment with id " + paymentId));

        switch (paymentToRevert.getPaymentStatus()) {
            case REVERTED:
                logger.info("Payment with id {} is already reverted", paymentToRevert.getPaymentId());
                return paymentToRevert.toPaymentDetailsDto();
            case DENIED:
                logger.error("Payment with id {} cannot be reverted. Has a denied status", paymentToRevert.getPaymentId());
                throw new IllegalArgumentException("Denied payment cannot be reverted");
            case ACCEPTED:
                if (isRequestApproved()) {
                    paymentToRevert.setPaymentStatus(PaymentStatus.REVERTED);
                    PaymentEntity updatedPaymentEntity = paymentRepository.save(paymentToRevert);
                    logger.info("Payment with id {} is reverted");
                    return updatedPaymentEntity.toPaymentDetailsDto();
                } else {
                    logger.error("Payment reversion cannot be processed");
                    throw new IllegalStateException("Payment reversion cannot be processed now. Try later");
                }
            default:
                throw new IllegalStateException("Unknown payment status");
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
