package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class OrderController {
    private final Logger logger = LoggerFactory.getLogger(LaundryController.class);

    private OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void submitOrder(@RequestBody OrderSubmissionDto order) {
        logger.info("Got submission request for order: {}", order);
        orderService.submitOrder(order);
    }

    @PutMapping
    public void completeOrder(@RequestParam int id) {
        logger.info("Got completion request for order with id: {}", id);
        orderService.completeOrder(id);
    }
}