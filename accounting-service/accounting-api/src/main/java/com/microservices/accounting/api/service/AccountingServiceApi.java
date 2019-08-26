package com.microservices.accounting.api.service;

import com.microservices.accounting.api.dto.InvokePaymentDto;
import com.microservices.accounting.api.dto.PaymentDetailsDto;

/**
 * An interface for accessing bank payment processing
 */
public interface AccountingServiceApi {

    /**
     * Invoke bank payment
     *
     * @throws NullPointerException if the input dto is null
     */
    PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto);

    /**
     * Revert bank payment
     *
     * @throws IllegalArgumentException if invalid paymentId is thrown
     * @throws IllegalArgumentException if payment was denied
     * @throws IllegalStateException    if service is not available
     */
    PaymentDetailsDto revertPayment(int paymentId);

}
