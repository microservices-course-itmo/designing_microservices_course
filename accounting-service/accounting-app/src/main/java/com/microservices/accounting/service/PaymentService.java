package com.microservices.accounting.service;

import com.microservices.accounting.api.dto.InvokePaymentDto;
import com.microservices.accounting.api.dto.PaymentDetailsDto;

public interface PaymentService {

    PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto);

    PaymentDetailsDto revertPayment(int paymentId);
}
