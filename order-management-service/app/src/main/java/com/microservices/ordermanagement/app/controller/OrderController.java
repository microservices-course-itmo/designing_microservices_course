package com.microservices.ordermanagement.app.controller;

import com.microservices.ordermanagement.app.api.OrderService;
import com.microservices.ordermanagement.app.dto.AddDetailDto;
import com.microservices.ordermanagement.app.dto.AssignTariffDto;
import com.microservices.ordermanagement.app.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;
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

    @PutMapping(value = "detail")
    OrderEntity addDetailToOrder(@Valid @RequestBody AddDetailDto addDetailDto) {
        return orderService.addDetailToOrder(addDetailDto);
    }

    @PutMapping(value = "tariff")
    OrderEntity assignTariffToOrderDetail(@Valid @RequestBody AssignTariffDto assignTariffDto) {
        return orderService.assignTariffToOrderDetail(assignTariffDto);
    }
}
