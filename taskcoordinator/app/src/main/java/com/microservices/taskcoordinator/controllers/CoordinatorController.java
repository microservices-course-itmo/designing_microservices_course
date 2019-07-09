package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class CoordinatorController {

    private OrderService orderService;

    private LaundryStateService laundryStateService;

    @PostMapping
    OrderSubmissionDto coordinateOrder(@RequestBody OrderCoordinationDto orderCoordinationDTO) {
        return orderService.coordinateOrder(orderCoordinationDTO);
    }

    @PutMapping("/{id}/status/submitted")
    void processSubmittedOrder(@RequestBody OrderSubmittedDto orderSubmittedDTO) {
        laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDTO);
    }

    @PutMapping("/{id}/status/processed")
    void processProcessedOrder(@RequestBody OrderProcessedDto orderProcessedDTO) {
        laundryStateService.updateLaundryStateWithOrderProcessed(orderProcessedDTO);
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setLaundryStateService(LaundryStateService laundryStateService) {
        this.laundryStateService = laundryStateService;
    }
}
