package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaundryStateServiceImpl implements LaundryStateService {

    //TODO inject through setter/constr
    @Autowired
    private LaundryStateRepository laundryStateRepository;


    @Override
    public LaundryStateDTO updateLaundryStateOrderSubmission(OrderSubmissionDTO orderSubmissionDTO) {
        return null;
    }

    @Override
    public LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO laundryState) {
        return null;
    }

    @Override
    public LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO laundryState) {
        return null;
    }

    @Override
    public LaundryStateEntity getLeastLoadedLaundry() {
        return laundryStateRepository.getLeastLoadedLaundry();
    }
}
