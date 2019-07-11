package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;

public interface LaundryStateService {

    LaundryStateDto getLaundryStateById(int laundryId);

    LaundryStateDto updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration);

    LaundryStateDto updateLaundryStateWithOrderSubmitted(OrderSubmittedDto laundryState);

    LaundryStateDto updateLaundryStateWithOrderProcessed(OrderProcessedDto laundryState);

    LaundryStateDto getLeastLoadedLaundry();

    long getCompletionTimePrediction(LaundryStateDto laundryStateDto);
}
