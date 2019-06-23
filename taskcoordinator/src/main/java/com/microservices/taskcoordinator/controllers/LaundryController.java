package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.OrderSubmissionDto;
import com.microservices.taskcoordinator.service.impl.LaundryService;
import com.microservices.taskcoordinator.service.impl.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coordinator")
public class LaundryController {

    private OrderService orderService;

    private LaundryService laundryService;

    @Autowired
    public LaundryController(OrderService orderService) {
        this.orderService = orderService;
    }

    /*@GetMapping
    public List<LaundryStateEntity> getLaundriesState() {
        return
    }*/

    @PostMapping
    public void submitOrder(@RequestBody OrderSubmissionDto order) {
        orderService.submitOrder(order);
    }
}
