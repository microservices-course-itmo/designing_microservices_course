package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface OrderMetricsService {

    void reportQueueChanged(LaundryStateEntity laundryStateEntity);

    void reportPredictionErrorAndAccuracy(OrderDto orderDto);

    void reportOrderSubmitted();

    void reportOrderCompleted();
}
