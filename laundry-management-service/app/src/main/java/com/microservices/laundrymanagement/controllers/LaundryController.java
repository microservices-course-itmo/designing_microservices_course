package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.LaundryService;
import com.microservices.laundrymanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("laundry")
public class LaundryController {
    private final Logger logger = LoggerFactory.getLogger(LaundryController.class);

    private OrderService orderService;
    private LaundryService laundryService;

    @Autowired
    public LaundryController(OrderService orderService, LaundryService laundryService) {
        this.orderService = orderService;
        this.laundryService = laundryService;
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

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int addLaundry(@RequestParam String laundryName) {
        logger.info("Got request for creating laundry with name \"{}\"", laundryName);
        return laundryService.registerLaundry(laundryName);
    }
}