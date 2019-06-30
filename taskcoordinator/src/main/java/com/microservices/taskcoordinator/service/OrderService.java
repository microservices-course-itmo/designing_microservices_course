package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import org.springframework.stereotype.Service;

public interface OrderService {

    OrderEntity getOrderById(Integer id);

    OrderSubmissionDTO coordinateOrder(OrderDTO order);

    OrderEntity updateOrderStatus(Integer orderEntityId, OrderStatus orderStatus);

}
