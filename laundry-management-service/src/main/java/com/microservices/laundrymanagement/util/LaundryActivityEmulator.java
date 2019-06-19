package com.microservices.laundrymanagement.util;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Semaphore;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class LaundryActivityEmulator {
    private Map<Integer, Semaphore> laundryWorkersLocks;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LaundryStateRepository laundryStateRepository;

    public void run() {
        List<Integer> ids = StreamSupport.stream(laundryStateRepository.findAll().spliterator(), false)
                .map(LaundryStateEntity::getId)
                .distinct()
                .collect(Collectors.toList());


    }

}


