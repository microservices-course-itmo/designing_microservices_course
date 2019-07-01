package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;

public interface OrderService {

    OrderEntity updateOrder(OrderEntity orderEntity);

    OrderEntity getOrderById(Integer id);

    OrderSubmissionDTO coordinateOrder(OrderCoordinationDTO order);

}
