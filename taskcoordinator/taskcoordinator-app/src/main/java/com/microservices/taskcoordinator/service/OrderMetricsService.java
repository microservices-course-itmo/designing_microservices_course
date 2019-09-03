package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface OrderMetricsService {

    // Updates values of queue waiting time and reserved time for designated laundry
    void reportQueueChanged(LaundryStateEntity laundryStateEntity);

    // Records error accuracy and error
    void reportPredictionErrorAndAccuracy(OrderDto orderDto);

    // Records submitted order (increments submitted orders counter)
    void reportOrderSubmitted();

    // Records completed order (decrements submitted orders counter)
    void reportOrderCompleted();
}
