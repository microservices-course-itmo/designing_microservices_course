package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface LaundryStateService {

    LaundryStateDto updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration);

    LaundryStateDto updateLaundryStateWithOrderSubmitted(OrderSubmittedDto laundryState);

    LaundryStateDto updateLaundryStateWithOrderProcessed(OrderProcessedDto laundryState);

    LaundryStateEntity getLeastLoadedLaundry();
}
