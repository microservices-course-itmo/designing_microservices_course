package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.service.OrderMetricsService;
import io.micrometer.core.instrument.DistributionSummary;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tags;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class OrderMetricsServiceImpl implements OrderMetricsService {

    private final Logger logger = LoggerFactory.getLogger(OrderMetricsServiceImpl.class);

    private MeterRegistry meterRegistry;

    private Map<Integer, Double> reservedQueue = new HashMap<>();

    private Map<Integer, Double> actualQueue = new HashMap<>();

    private DistributionSummary predictionAccuracy;

    private DistributionSummary predictionError;

    private AtomicInteger submittedOrders = new AtomicInteger(0);

    OrderMetricsServiceImpl(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        predictionAccuracy = meterRegistry.summary("prediction.accuracy");
        predictionError = meterRegistry.summary("prediction.error");
        meterRegistry.gauge("orders.submitted", submittedOrders);
    }

    @Override
    public void reportQueueChanged(LaundryStateEntity laundryStateEntity) {
        if(!actualQueue.containsKey(laundryStateEntity.getId()))
            registerQueuesGauges(laundryStateEntity.getId());

        actualQueue.put(laundryStateEntity.getId(), (double) laundryStateEntity.getQueueWaitingTime());
        reservedQueue.put(laundryStateEntity.getId(), (double) laundryStateEntity.getReservedTime());

        logger.info("For laundryId {} reserved queue and actual queues are {} and {}", laundryStateEntity.getId(),
                laundryStateEntity.getReservedTime(), laundryStateEntity.getQueueWaitingTime());
    }

    @Override
    public void reportPredictionErrorAndAccuracy(OrderDto order) {
        double error = Math.abs(order.getCompletionTime() - order.getEstimatedTime());

        predictionError.record(error);
        predictionAccuracy.record(1 - error / order.getEstimatedTime());

        logger.info("Prediction accuracy: {}", 1 - error / order.getEstimatedTime());
    }

    public void reportOrderSubmitted(){
        submittedOrders.incrementAndGet();
    }

    public void reportOrderCompleted(){
        submittedOrders.decrementAndGet();
    }

    private void registerQueuesGauges(int laundryId) {
        meterRegistry.gauge("queue.actual", Tags.of("laundry.id", Integer.toString(laundryId)), actualQueue, (q) -> q.get(laundryId));
        meterRegistry.gauge("queue.reserved", Tags.of("laundry.id", Integer.toString(laundryId)), reservedQueue, (q) -> q.get(laundryId));
    }
}
