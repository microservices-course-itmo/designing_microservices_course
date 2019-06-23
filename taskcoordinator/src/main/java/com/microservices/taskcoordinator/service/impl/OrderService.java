package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.OrderEntity;

public interface OrderService {

    OrderEntity submitOrder(OrderSubmissionDto orderSubmissionDto);

}
