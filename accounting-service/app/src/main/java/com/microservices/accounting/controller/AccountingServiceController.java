package com.microservices.accounting.controller;

import com.microservices.accounting.dto.InvokePaymentDto;
import com.microservices.accounting.dto.PaymentDetailsDto;
import com.microservices.accounting.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@RequestMapping("/accounting")
public class AccountingServiceController {
    private final PaymentService paymentService;

    @Autowired
    public AccountingServiceController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public PaymentDetailsDto invokePayment(@Valid @RequestBody InvokePaymentDto invokePaymentDto) {
        return paymentService.invokePayment(invokePaymentDto);
    }

    @PutMapping("/cancel/{paymentId}")
    public PaymentDetailsDto revertPayment(@PathVariable int paymentId) {
        return paymentService.revertPayment(paymentId);
    }
}
