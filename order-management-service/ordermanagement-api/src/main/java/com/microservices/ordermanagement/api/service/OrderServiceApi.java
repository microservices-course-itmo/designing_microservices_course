package com.microservices.ordermanagement.api.service;

import com.microservices.ordermanagement.api.dto.AddDetailDto;
import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.dto.PaymentDetailsDto;

/**
 * Service that provides an access for order management
 */
public interface OrderServiceApi {
    /**
     * Bind existing pending detail with existing order.
     * in case there is no such detail thrown {@link IllegalArgumentException}
     * in case there is no such order creates new order in underlying database
     */
    OrderDto addDetailToOrder(AddDetailDto addDetailDto);

    /**
     * Assign given tariff to detail associated with given id in order associated with given id
     * If no such order exists throws {@link IllegalArgumentException}
     * If no such detail in order exists throws {@link IllegalArgumentException}
     */
    OrderDto assignTariffToOrderDetail(AssignTariffDto assignTariffDto);

    /**
     * Check some invariants for order and if all is good approve order and publish order created event to event bus.
     */
    OrderDto approveOrder(int orderId, PaymentDetailsDto paymentDetailsDto);
}
