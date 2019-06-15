package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.dto.OrderStatus;
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
    public int createOrder(OrderRequest request) {
        return orderService.createOrder(request);
    }

    @GetMapping
    public OrderStatus getStatus(@RequestParam int id) {
        return orderService.getStatus(id);
    }

    @PutMapping
    public void completeOrder(@RequestParam int id) {
        orderService.completeOrder(id);
    }
}
