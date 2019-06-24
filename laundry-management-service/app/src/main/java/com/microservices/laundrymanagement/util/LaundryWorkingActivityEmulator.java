package com.microservices.laundrymanagement.util;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class LaundryWorkingActivityEmulator {

    private final LaundryStateRepository laundryStateRepository;

    private final OrderService orderService;

    private final Logger logger = LoggerFactory.getLogger(LaundryWorkingActivityEmulator.class);

    @Autowired
    public LaundryWorkingActivityEmulator(LaundryStateRepository laundryStateRepository, OrderService orderService) {
        this.laundryStateRepository = laundryStateRepository;
        this.orderService = orderService;
    }

    public void run() throws ExecutionException, InterruptedException {
        List<Integer> ids = StreamSupport.stream(laundryStateRepository.findAll().spliterator(), false)
                .map(LaundryStateEntity::getId)
                .distinct()
                .collect(Collectors.toList());

        logger.info("Creating emulators for queues with following ids: {}", ids);

        CompletableFuture.allOf(ids.stream()
                .map(id -> CompletableFuture.runAsync(new LaundryWorker(orderService, id)))
                .toArray(CompletableFuture[]::new)).get();
    }

    static class LaundryWorker implements Runnable {
        private OrderService orderService;
        private int laundryId;

        LaundryWorker(OrderService orderService, int laundryId) {
            this.orderService = orderService;
            this.laundryId = laundryId;
        }

        @Override
        public void run() {
            while (true) {
                orderService.completeNextOrderInQueue(laundryId);
            }
        }
    }

}
