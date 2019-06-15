package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.dto.OrderStatus;

public interface OrderService {
    int createOrder(OrderRequest orderRequest);

    OrderStatus getStatus(int orderId);

    void completeOrder(int orderId);
}
