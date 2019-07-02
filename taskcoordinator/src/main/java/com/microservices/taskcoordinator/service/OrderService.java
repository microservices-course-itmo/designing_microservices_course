package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.OrderDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;

public interface OrderService {

    OrderDTO updateOrder(OrderDTO order);

    OrderDTO getOrderById(Integer id);

    OrderSubmissionDTO coordinateOrder(OrderCoordinationDTO inboundOrder);

}
