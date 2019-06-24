package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.*;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderCompletedMessageRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.repository.OrderSubmittedMessageRepository;
import com.microservices.laundrymanagement.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private LaundryStateRepository laundryStateRepository;
    private OrderSubmittedMessageRepository orderSubmittedMessageRepository;
    private OrderCompletedMessageRepository orderCompletedMessageRepository;

    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            LaundryStateRepository laundryStateRepository,
                            OrderSubmittedMessageRepository orderSubmittedMessageRepository,
                            OrderCompletedMessageRepository orderCompletedMessageRepository) {
        this.orderRepository = orderRepository;
        this.laundryStateRepository = laundryStateRepository;
        this.orderSubmittedMessageRepository = orderSubmittedMessageRepository;
        this.orderCompletedMessageRepository = orderCompletedMessageRepository;
    }

    @Transactional
    @Override
    public void submitOrder(OrderSubmissionDto orderSubmissionDto) {
        logger.info("Submitting order: {}...", orderSubmissionDto);
        OrderEntity orderEntity = new OrderEntity(orderSubmissionDto);
        if (orderRepository.existsById(orderSubmissionDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderSubmissionDto.getOrderId() +
                    "already exists");
        orderRepository.save(orderEntity);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(orderEntity, RequestType.SUBMIT);

        OrderSubmittedMessageEntity orderSubmittedMessageEntity = new OrderSubmittedMessageEntity(
                orderEntity.getId(), laundryStateEntity);
        orderSubmittedMessageRepository.save(orderSubmittedMessageEntity);
        logger.info("Order with id {} is submitted", orderSubmissionDto.getOrderId());
    }

    @Transactional
    @Override
    public void completeOrder(int id) {
        logger.info("Completing order {}...", id);
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + id + " is not found")); //todo: logs
        if (order.getStatus() == OrderStatus.COMPLETE) {
            logger.warn("Order with id {} is already completed");
            return;
        }

        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(order, RequestType.COMPLETE);

        OrderCompletedMessageEntity orderCompletedMessage = new OrderCompletedMessageEntity(
                order, laundryStateEntity);
        orderCompletedMessageRepository.save(orderCompletedMessage);
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
