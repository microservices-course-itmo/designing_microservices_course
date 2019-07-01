package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface LaundryStateService {

    LaundryStateDTO updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration);

    LaundryStateEntity updateLaundryStateWithOrderSubmitted(OrderSubmittedDTO laundryState);

    LaundryStateEntity updateLaundryStateWithOrderProcessed(OrderProcessedDTO laundryState);

    LaundryStateEntity getLeastLoadedLaundry();
}
