package com.microservices.ordermanagement.app.controller;

import com.microservices.ordermanagement.api.dto.AddDetailDto;
import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.dto.OrderStatus;
import com.microservices.ordermanagement.api.dto.PaymentDetailsDto;
import com.microservices.ordermanagement.app.api.OrderService;
import com.microservices.ordermanagement.app.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("order")
public class OrderController {
    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(value = "{orderId}")
    OrderEntity getOrderById(@PathVariable int orderId) {
        return orderService.getOrderById(orderId);
    }

    @PutMapping(value = "detail")
    OrderDto addDetailToOrder(@Valid @RequestBody AddDetailDto addDetailDto) {
        return orderService.addDetailToOrder(addDetailDto);
    }

    @PutMapping(value = "tariff")
    OrderDto assignTariffToOrderDetail(@Valid @RequestBody AssignTariffDto assignTariffDto) {
        return orderService.assignTariffToOrderDetail(assignTariffDto);
    }

    @PutMapping(value = "{orderId}/status/{status}")
    OrderDto setOrderStatus(@PathVariable("orderId") int orderId, @PathVariable("status") OrderStatus status) {
        return orderService.changeOrderStatus(orderId, status);
    }

    @PutMapping(value = "approve/{orderId}")
    OrderDto approveOrder(@PathVariable("orderId") int orderId, @RequestBody @Valid PaymentDetailsDto paymentDetailsDto) {
        return orderService.checkValidAndApprove(orderId, paymentDetailsDto);
    }
}
