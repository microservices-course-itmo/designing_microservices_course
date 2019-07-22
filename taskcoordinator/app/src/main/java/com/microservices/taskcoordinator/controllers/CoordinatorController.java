package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("orders")
public class CoordinatorController {

    private OrderService orderService;

    private LaundryStateService laundryStateService;

    private static final String INCONSISTENT_ID_ERROR_MESSAGE = "Order id in path and in DTO must be equal";

    @PostMapping
    OrderSubmissionDto coordinateOrder(@Valid @RequestBody OrderCoordinationDto orderCoordinationDTO) {
        return orderService.coordinateOrder(orderCoordinationDTO);
    }

    @PutMapping("/{id}/status/submitted")
    void processSubmittedOrder(@Valid @RequestBody OrderSubmittedDto orderSubmittedDTO,
                               @PathVariable int id) {
        if (id != orderSubmittedDTO.getOrderId()) {
            throw new IllegalArgumentException(INCONSISTENT_ID_ERROR_MESSAGE);
        }
        laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDTO);
    }

    @PutMapping("/{id}/status/processed")
    void processProcessedOrder(@Valid @RequestBody OrderProcessedDto orderProcessedDTO,
                               @PathVariable int id) {
        if (id != orderProcessedDTO.getOrderId()) {
            throw new IllegalArgumentException(INCONSISTENT_ID_ERROR_MESSAGE);
        }
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
