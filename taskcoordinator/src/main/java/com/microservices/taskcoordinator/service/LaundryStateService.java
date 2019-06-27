package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;

public interface LaundryStateService {

    LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO laundryState);

    LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO laundryState);
}
