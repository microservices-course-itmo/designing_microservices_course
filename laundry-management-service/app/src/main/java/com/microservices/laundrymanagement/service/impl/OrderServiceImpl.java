package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.service.LaundryEventPublishingService;
import com.microservices.laundrymanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;

    private LaundryStateRepository laundryStateRepository;

    private LaundryEventPublishingService eventPublishingService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            LaundryStateRepository laundryStateRepository,
                            LaundryEventPublishingService eventPublishingService) {
        this.orderRepository = orderRepository;
        this.laundryStateRepository = laundryStateRepository;
        this.eventPublishingService = eventPublishingService;
    }

    @Transactional
    @Override
    // TODO sukhoa : as this method invocation results in publishing message shouldn't we return It (dto) as a returning value here?
    @NewSpan(name = "submit_order")
    public void submitOrder(@SpanTag("order.dto") OrderSubmissionDto orderSubmissionDto) {
        Objects.requireNonNull(orderSubmissionDto);

        if (orderRepository.existsById(orderSubmissionDto.getOrderId())) {
            logger.error("Order with id {} already exists", orderSubmissionDto.getOrderId());
            throw new IllegalArgumentException("Order with id " + orderSubmissionDto.getOrderId() +
                    " already exists");
        }

        logger.info("Submitting order: {}", orderSubmissionDto);
        OrderEntity orderEntity = new OrderEntity(orderSubmissionDto);
        orderRepository.save(orderEntity);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(orderEntity, RequestType.SUBMIT);
        logger.info("Order with id {} is submitted", orderSubmissionDto.getOrderId());

        eventPublishingService.buildAndPublishOrderSubmittedEvent(orderEntity.getId(), laundryStateEntity);
    }

    @Transactional
    @Override
    public void completeOrder(int id) {
        logger.info("Completing order {}...", id);
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Order with id " + id + " is not found");
                    return new IllegalArgumentException("Order with id " + id + " is not found");
                });
        if (order.getStatus() == OrderStatus.COMPLETE) {
            logger.warn("Order with id {} is already completed", order.getId());
            return;
        }

        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(order, RequestType.COMPLETE);

        eventPublishingService.buildAndPublishOrderProcessedEvent(order.getId(), laundryStateEntity);
        logger.info("Order with id {} is completed", id);
    }

    private LaundryStateEntity updateQueueInfo(OrderEntity order, RequestType requestType) {
        int queueId = order.getLaundryId();
        LaundryStateEntity laundryState = laundryStateRepository.findById(queueId)
                .orElseThrow(() -> new IllegalArgumentException("Queue with id " + queueId + "is not found"));

        switch (requestType) {
            case SUBMIT:
                laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() + order.getEstimatedTime());
                break;
            case COMPLETE:
                laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() - order.getEstimatedTime());
                break;
        }

        return laundryStateRepository.save(laundryState);

    }

    @Transactional
    public void completeNextOrderInQueue(int laundryId) {
        Optional<OrderEntity> nextOrderInQueue = orderRepository.findNextIncompleteOrderInQueue(laundryId);
        if (!nextOrderInQueue.isPresent()) {
            logger.debug("The queue {} is empty. Cannot find next order to treat", laundryId);
            return;
        }
        OrderEntity orderEntity = nextOrderInQueue.get();
        logger.info("Start processing order {} from queue {}", orderEntity.getId(), laundryId);
        orderEntity.setStatus(OrderStatus.IN_PROCESS);
        orderRepository.save(orderEntity);
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(orderEntity.getEstimatedTime());
                break;
            } catch (InterruptedException ignore) {

            }
        }
        this.completeOrder(orderEntity.getId());
    }

}
