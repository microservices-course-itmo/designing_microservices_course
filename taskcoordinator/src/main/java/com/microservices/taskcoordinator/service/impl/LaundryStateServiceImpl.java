package com.microservices.taskcoordinator.service.impl;

import com.google.common.collect.Lists;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collections;

public class LaundryStateServiceImpl implements LaundryStateService {

    //TODO inject through setter/constr
    @Autowired
    private LaundryStateRepository laundryStateRepository;

    @Override
    public LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO laundryState) {
        return null;
    }

    @Override
    public LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO laundryState) {
        return null;
    }

    public long getLeastLoadedLaundry() {
        Iterable<LaundryStateEntity> allLaundriesStates = Lists.newArrayList(laundryStateRepository.findAll());
        return 0;
    }
}
