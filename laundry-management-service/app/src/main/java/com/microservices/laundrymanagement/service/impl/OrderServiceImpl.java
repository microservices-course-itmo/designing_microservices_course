package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.*;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderCompletedMessageRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.repository.OrderSubmittedMessageRepository;
import com.microservices.laundrymanagement.service.OrderService;
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
        OrderEntity orderEntity = new OrderEntity(orderSubmissionDto);
        if (orderRepository.existsById(orderSubmissionDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderSubmissionDto.getOrderId() +
                    "already exists");
        orderRepository.save(orderEntity);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(orderEntity, RequestType.SUBMIT);

        OrderSubmittedMessageEntity orderSubmittedMessageEntity = new OrderSubmittedMessageEntity(
                orderEntity.getId(), laundryStateEntity);
        orderSubmittedMessageRepository.save(orderSubmittedMessageEntity);
    }

    @Transactional
    @Override
    public void completeOrder(int id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + id + " is not found")); //todo: logs
        if (order.getStatus() == OrderStatus.COMPLETE) {
            return;
        }

        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(order, RequestType.COMPLETE);

        OrderCompletedMessageEntity orderCompletedMessage = new OrderCompletedMessageEntity(
                order, laundryStateEntity);
        orderCompletedMessageRepository.save(orderCompletedMessage);
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
            // TODO shine2 log this fact
            return;
        }
        OrderEntity orderEntity = nextOrderInQueue.get();
        while (true) {
            try {
                TimeUnit.MILLISECONDS.sleep(orderEntity.getEstimatedTime());
                break;
            } catch (InterruptedException ignore) {
                //TODO shine2
            }
        }
        this.completeOrder(orderEntity.getId());
    }

}
