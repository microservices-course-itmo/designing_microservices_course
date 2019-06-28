package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import org.springframework.stereotype.Service;

public interface OrderService {

    OrderSubmissionDTO coordinateOrder(OrderDTO order);

}
