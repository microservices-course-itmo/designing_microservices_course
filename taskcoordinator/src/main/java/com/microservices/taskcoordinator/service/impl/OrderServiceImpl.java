package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;
import org.springframework.beans.factory.annotation.Autowired;

public class OrderServiceImpl implements OrderService {

    @Autowired
    private PredictionService predictionService;

    @Autowired
    private LaundryService laundryService;

    @Override
    public OrderEntity submitOrder(OrderDTO orderDTO) {
        return null;
    }
}
