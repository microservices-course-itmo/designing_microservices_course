package com.microservices.taskcoordinator.controllers;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCompletedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private final Logger logger = LoggerFactory.getLogger(CoordinatorController.class);

    private OrderService orderService;

    private LaundryStateService laundryStateService;

    private static final String INCONSISTENT_ID_ERROR_MESSAGE = "Order id in path and in DTO must be equal";

    @PostMapping
    OrderSubmissionDto coordinateOrder(@Valid @RequestBody OrderCoordinationDto orderCoordinationDTO) {
        logger.info("Received order-coordination request for orderId = {}", orderCoordinationDTO.getOrderId());
        return orderService.coordinateOrder(orderCoordinationDTO);
    }

    @PutMapping("/{id}/status/submitted")
    void processSubmittedOrder(@Valid @RequestBody OrderSubmittedDto orderSubmittedDTO,
                               @PathVariable int id) {
        logger.info("Received submitted order to process: {}", orderSubmittedDTO);
        if (id != orderSubmittedDTO.getOrderId()) {
            throw new IllegalArgumentException(INCONSISTENT_ID_ERROR_MESSAGE);
        }
        laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDTO);
    }

    @PutMapping("/{id}/status/completed")
    void processCompletedOrder(@Valid @RequestBody OrderProcessedDto orderProcessedDTO,
                               @PathVariable int id) {
        logger.info("Received completed order to process: {}", orderCompletedDTO);
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
