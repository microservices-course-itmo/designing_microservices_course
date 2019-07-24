package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCompletedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;

public interface LaundryStateService {

    LaundryStateDto getLaundryStateById(int laundryId);

    LaundryStateDto updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration);

    LaundryStateDto updateLaundryStateWithOrderSubmitted(OrderSubmittedDto laundryState);

    LaundryStateDto updateLaundryStateWithOrderProcessed(OrderCompletedDto laundryState);

    LaundryStateDto getLeastLoadedLaundry();

    long getCompletionTimePrediction(LaundryStateDto laundryStateDto);
}
