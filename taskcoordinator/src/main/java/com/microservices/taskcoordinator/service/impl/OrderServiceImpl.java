package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.repository.OrderRepository;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService {

    //TODO
    @Autowired
    private OrderRepository orderRepository;

    //TODO FormattedExceptions
    @Override
    public OrderSubmissionDTO coordinateOrder(OrderDTO orderDto) {
        if (orderRepository.existsById(orderDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderDto.getOrderId() + "already exists");

        return null;

    }

}
