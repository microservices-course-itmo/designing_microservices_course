package com.microservices.ordermanagement.app.api;


import com.microservices.ordermanagement.app.entity.OrderEntity;

public interface OrderManagementEventPublishingService {
    void buildAndPublishOrderCreatedEvent(OrderEntity order);
}
