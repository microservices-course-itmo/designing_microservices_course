package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;

public interface OrderService {

    OrderEntity submitOrder(OrderDTO orderDTO);

}
