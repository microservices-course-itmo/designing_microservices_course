package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderRequest;
import com.microservices.laundrymanagement.model.Order;
import com.microservices.laundrymanagement.model.OrderStatus;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public Order createOrder(OrderRequest orderRequest) {
        Order order = new Order(orderRequest, OrderStatus.QUEUED);
        return orderRepository.save(order);
    }

    @Override
    public void completeOrder(int id) {
        changeStatus(id, OrderStatus.COMPLETE);
    }

    private void changeStatus(int orderId, OrderStatus status) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (!order.isPresent()) {
            throw new IllegalArgumentException();
        }
        order.get().setStatus(status);
        orderRepository.save(order.get());
    }
}
