package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.dto.OrderStatus;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {
    @Override
    public int createOrder(OrderRequest orderRequest) {
        return 0;
    }

    @Override
    public OrderStatus getStatus(int id) {
        return null;
    }

    @Override
    public void completeOrder(int id) {

    }
}
