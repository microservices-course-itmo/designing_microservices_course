package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.model.Order;
import com.microservices.laundrymanagement.model.OrderStatus;

public interface OrderService {
    Order createOrder(OrderRequest orderRequest);

    OrderStatus getStatus(int orderId);

    void completeOrder(int orderId);

    void deliverOrder(int orderId);

    void processOrder(int orderId);
}
