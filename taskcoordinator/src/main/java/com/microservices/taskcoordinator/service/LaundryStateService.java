package com.microservices.taskcoordinator.service;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import org.springframework.stereotype.Service;

public interface LaundryStateService {

    LaundryStateDTO updateLaundryStateOrderSubmission(OrderSubmissionDTO orderSubmissionDTO);

    LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO laundryState);

    LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO laundryState);

    LaundryStateEntity getLeastLoadedLaundry();
}
