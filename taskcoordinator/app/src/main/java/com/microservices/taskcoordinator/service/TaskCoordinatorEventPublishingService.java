package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;

public interface TaskCoordinatorEventPublishingService {
    void buildAndPublishOrderSubmissionEvent(OrderSubmissionDto orderSubmissionDto);
}
