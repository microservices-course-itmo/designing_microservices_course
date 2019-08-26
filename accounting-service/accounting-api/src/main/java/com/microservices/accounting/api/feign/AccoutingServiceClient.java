package com.microservices.accounting.api.feign;

import com.microservices.accounting.api.dto.InvokePaymentDto;
import com.microservices.accounting.api.dto.PaymentDetailsDto;
import com.microservices.accounting.api.service.AccountingServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * Defines the parameters and paths of REST API of tariff management api
 * <p>
 * Java feign client will be generated based on this declaration.
 * <p>
 * We use placeholder in the {@link FeignClient#name()} field in order
 * for Ribbon load balancing client and Eureka discovery service to understand
 * the name of the service the request should be redirected to.
 */
@FeignClient(name = "${accounting.service.name}/accounting")
public interface AccoutingServiceClient extends AccountingServiceApi {

    /**
     * {@inheritDoc}
     */
    @PostMapping
    PaymentDetailsDto invokePayment(@Valid @RequestBody InvokePaymentDto invokePaymentDto);

    /**
     * {@inheritDoc}
     */
    @PutMapping("/cancel/{paymentId}")
    PaymentDetailsDto revertPayment(@PathVariable int paymentId);
}
