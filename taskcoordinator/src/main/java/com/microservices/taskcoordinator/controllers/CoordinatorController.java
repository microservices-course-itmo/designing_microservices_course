package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
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
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("orders")
public class CoordinatorController {

    private final OrderService orderService;

    private final LaundryStateService laundryStateService;

    @Autowired
    public CoordinatorController(OrderService orderService, LaundryStateService laundryStateService) {
        this.orderService = orderService;
        this.laundryStateService = laundryStateService;
    }

    @PostMapping()
    OrderSubmissionDTO coordinateOrder(@RequestBody OrderCoordinationDTO orderCoordinationDTO) {
        return orderService.coordinateOrder(orderCoordinationDTO);
    }

    @PutMapping("/{id}/status/submitted")
    void processSubmittedOrder(@RequestBody OrderSubmittedDTO orderSubmittedDTO) {
        laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDTO);
    }

    @PutMapping("/{id}/status/processed")
    void processProcessedOrder(@RequestBody OrderProcessedDTO orderProcessedDTO) {
        laundryStateService.updateLaundryStateWithOrderProcessed(orderProcessedDTO);
    }


}
