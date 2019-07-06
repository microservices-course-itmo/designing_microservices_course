package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;

public interface OrderService {

    OrderDto updateOrder(OrderDto order);

    OrderDto getOrderById(Integer id);

    OrderSubmissionDto coordinateOrder(OrderCoordinationDto inboundOrder);

}
