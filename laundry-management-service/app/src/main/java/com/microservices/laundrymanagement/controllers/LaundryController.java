package com.microservices.laundrymanagement.controllers;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.LaundryService;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("laundry")
public class LaundryController {
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
        orderService.submitOrder(order);
    }

    @PutMapping
    public void completeOrder(@RequestParam int id) {
        orderService.completeOrder(id);
    }

    @PostMapping("/new")
    @ResponseStatus(HttpStatus.CREATED)
    public int addLaundry() {
        return laundryService.addNewLaundry();
    }
}
