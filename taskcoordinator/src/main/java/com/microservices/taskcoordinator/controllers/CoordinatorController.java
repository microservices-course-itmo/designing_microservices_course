package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/coordinator")
public class CoordinatorController {

    private OrderService orderService;

    private LaundryStateService laundryStateService;

    @PostMapping(name = "/orders")
    OrderSubmissionDTO coordinateOrder(OrderDTO orderDTO) {
        return orderService.coordinateOrder(orderDTO);
    }

    @PutMapping(name = "/orders/{id}/status/submitted")
    void processSubmittedOrder(OrderSubmittedDTO orderSubmittedDTO) {
        laundryStateService.updateLaundryStateOrderSubmitted(orderSubmittedDTO);
    }

    //TODO: if we don't return dto, then has to be any response-message-class's object to return
    @PutMapping(name = "/orders/{id}/status/processed")
    void processProcessedOrder(OrderProcessedDTO orderProcessedDTO) {
        laundryStateService.updateLaundryStateOrderProcessed(orderProcessedDTO);
    }


}
