package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("laundry")
public class LaundryController {
    private OrderService orderService;

    @Autowired
    public LaundryController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public void submitOrder(@RequestBody OrderSubmissionDto order) {
        orderService.submitOrder(order);
    }

    @PutMapping
    public void completeOrder(@RequestParam int id) {
        orderService.completeOrder(id);
    }
}
