package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface LaundryStateService {

    LaundryStateDTO updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration);

    LaundryStateDTO updateLaundryStateWithOrderSubmitted(OrderSubmittedDTO laundryState);

    LaundryStateDTO updateLaundryStateWithOrderProcessed(OrderProcessedDTO laundryState);

    LaundryStateEntity getLeastLoadedLaundry();
}
