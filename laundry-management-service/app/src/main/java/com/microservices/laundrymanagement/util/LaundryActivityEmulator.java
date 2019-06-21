package com.microservices.laundrymanagement.util;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Component
public class LaundryActivityEmulator {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LaundryStateRepository laundryStateRepository;

    @Autowired
    private OrderService orderService;

    public void run() throws ExecutionException, InterruptedException {
        List<Integer> ids = StreamSupport.stream(laundryStateRepository.findAll().spliterator(), false)
                .map(LaundryStateEntity::getId)
                .distinct()
                .collect(Collectors.toList());

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

        }
    }

}


