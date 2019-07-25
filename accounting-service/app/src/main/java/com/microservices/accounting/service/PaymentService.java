package com.microservices.accounting.service;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.dto.RevertPaymentDto;

import javax.servlet.UnavailableException;

public interface PaymentService {

    PaymentDetailsDto invokePayment(InvokePaymentDto invokePaymentDto);

    PaymentDetailsDto revertPayment(RevertPaymentDto revertPaymentDto) throws UnavailableException;
}
