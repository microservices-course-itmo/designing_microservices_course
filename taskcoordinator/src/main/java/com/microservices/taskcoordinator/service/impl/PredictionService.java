package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import org.springframework.stereotype.Service;

@Service
public class PredictionService {

    public long getOrderCompletionTimePrediction(LaundryStateEntity laundryStateEntity) {
        return laundryStateEntity.getQueueWaitingTime() + laundryStateEntity.getReservedTime();
    }
}
