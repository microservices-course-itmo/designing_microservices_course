package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.model.Order;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);

    void completeOrder(int orderId);
}
