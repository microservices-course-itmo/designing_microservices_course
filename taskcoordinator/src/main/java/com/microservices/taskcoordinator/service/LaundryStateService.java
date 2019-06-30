package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderEntity;
import org.springframework.stereotype.Service;

public interface LaundryStateService {

    LaundryStateDTO updateLaundryStateByOrderSubmission(OrderEntity orderEntity);

    LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO laundryState);

    LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO laundryState);

    LaundryStateEntity getLeastLoadedLaundry();
}
