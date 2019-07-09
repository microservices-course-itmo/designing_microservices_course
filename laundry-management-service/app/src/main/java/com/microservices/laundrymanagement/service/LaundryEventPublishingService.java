package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;

public interface LaundryEventPublishingService {
    void buildAndPublishOrderSubmittedEvent(int orderId, LaundryStateEntity laundryState);

    void buildAndPublishOrderProcessedEvent(int orderId, LaundryStateEntity laundryState);
}
