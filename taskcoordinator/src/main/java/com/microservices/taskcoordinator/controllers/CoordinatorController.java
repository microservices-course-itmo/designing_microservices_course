package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("coordinator")
public class CoordinatorController {

    private final OrderService orderService;

    private final LaundryStateService laundryStateService;

    @Autowired
    public CoordinatorController(OrderService orderService, LaundryStateService laundryStateService) {
        this.orderService = orderService;
        this.laundryStateService = laundryStateService;
    }

    @PostMapping(name = "/orders")
    OrderSubmissionDTO coordinateOrder(@RequestBody OrderDTO orderDTO) {
        return orderService.coordinateOrder(orderDTO);
    }

    @PutMapping("/orders/{id}/status/submitted")
    void processSubmittedOrder(@RequestBody OrderSubmittedDTO orderSubmittedDTO) {
        laundryStateService.updateLaundryStateOrderSubmitted(orderSubmittedDTO);
    }

    //TODO: if we don't return dto, then has to be any response-message-class's object to return
    @PutMapping("/orders/{id}/status/processed")
    void processProcessedOrder(@RequestBody OrderProcessedDTO orderProcessedDTO) {
        laundryStateService.updateLaundryStateOrderProcessed(orderProcessedDTO);
    }


}
