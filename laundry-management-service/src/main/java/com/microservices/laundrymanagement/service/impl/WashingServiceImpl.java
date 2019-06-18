package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.model.Laundry;
import com.microservices.laundrymanagement.model.LaundryStatus;
import com.microservices.laundrymanagement.model.Order;
import com.microservices.laundrymanagement.service.LaundryService;
import com.microservices.laundrymanagement.service.OrderService;
import com.microservices.laundrymanagement.service.WashingService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class WashingServiceImpl implements WashingService {
    private LaundryService laundryService;
    private OrderService orderService;

    public WashingServiceImpl(LaundryService laundryService, OrderService orderService) {
        this.laundryService = laundryService;
        this.orderService = orderService;
    }

    @Override
    public void start() {
        List<Integer> laundries = laundryService.getAllLaundries().stream()
                .map(Laundry::getId)
                .collect(Collectors.toList());

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(laundries.size());
        laundries.forEach(el ->
                executorService.scheduleAtFixedRate(new LaundryCallable(el), 0, 10, TimeUnit.MILLISECONDS)
        );
    }

    private void startOneLaundry(int laundryId) {
        Optional<Laundry> byId = laundryService.getById(laundryId);
        if (byId.isPresent()) {
            if (byId.get().getStatus() == LaundryStatus.FREE) {
                Optional<Order> nextOrder = laundryService.selectNextOrder(laundryId);
                nextOrder.ifPresent(this::wash);
            }
        }
    }

    private void wash(Order order) {
        try {
            orderService.processOrder(order.getOrderId());
            Thread.sleep(order.getEstimatedTime());
            orderService.completeOrder(order.getOrderId());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    class LaundryCallable implements Runnable {
        private int id;

        LaundryCallable(int id) {
            this.id = id;
        }

        @Override
        public void run() {
            startOneLaundry(id);
        }
    }
}
