package com.microservices.ordermanagement.api.feign;

import com.microservices.ordermanagement.api.dto.AddDetailDto;
import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.dto.PaymentDetailsDto;
import com.microservices.ordermanagement.api.service.OrderServiceApi;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;

/**
 * Defines the parameters and paths of REST API of order-management-service
 * <p>
 * Java feign client will be generated based on this declaration.
 * <p>
 * We use placeholder in the {@link FeignClient#name()} (same as {@link FeignClient#value()) field in order
 * for Ribbon load balancing client and Eureka discovery service to understand
 * the name of the service the request should be redirected to.
 */
@FeignClient("${order.management.service.name}/order")
public interface OrderServiceClient extends OrderServiceApi {

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "detail")
    OrderDto addDetailToOrder(AddDetailDto addDetailDto);

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "tariff")
    OrderDto assignTariffToOrderDetail(AssignTariffDto assignTariffDto);

    /**
     * {@inheritDoc}
     */
    @PutMapping(value = "approve/{orderId}")
    OrderDto approveOrder(@PathVariable("orderId") int orderId, PaymentDetailsDto paymentDetailsDto);
}
