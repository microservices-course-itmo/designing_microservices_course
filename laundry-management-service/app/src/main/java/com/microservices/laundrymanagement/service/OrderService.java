package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;

public interface OrderService {
    void submitOrder(OrderSubmissionDto orderSubmissionDto);

    void completeOrder(int orderId);

    void completeNextOrderInQueue(int laundryId);
}
