package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.entity.OrderCompletedMessageEntity;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import com.microservices.laundrymanagement.entity.OrderSubmittedMessageEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderCompletedMessageRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.repository.OrderSubmittedMessageRepository;
import com.microservices.laundrymanagement.service.OrderService;
import com.microservices.laundrymanagementapi.messages.LaundryState.LaundryStateMessage;
import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Service
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(OrderServiceImpl.class);

    private OrderRepository orderRepository;
    private LaundryStateRepository laundryStateRepository;
    private OrderSubmittedMessageRepository orderSubmittedMessageRepository;
    private OrderCompletedMessageRepository orderCompletedMessageRepository;

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
        if (orderRepository.existsById(orderSubmissionDto.getOrderId())) {
            logger.error("Order with id {} already exists", orderSubmissionDto.getOrderId());
            throw new IllegalArgumentException("Order with id " + orderSubmissionDto.getOrderId() +
                    " already exists");
        }
        orderRepository.save(orderEntity);


        LaundryStateEntity laundryStateEntity = updateQueueInfo(orderEntity, RequestType.SUBMIT);

        OrderSubmittedEvent.OrderSubmittedMessage orderSubmittedMessage = OrderSubmittedEvent.OrderSubmittedMessage.newBuilder()
                .setOrderId(orderEntity.getId())
                .setState(LaundryStateMessage.newBuilder()
                        .setLaundryId(laundryStateEntity.getId())
                        .setQueueWaitingTime(laundryStateEntity.getQueueWaitingTime())
                        .setVersion(laundryStateEntity.getVersion()))
                .build();

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
