package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import com.microservices.laundrymanagement.entity.OrderSubmittedMessageEntity;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.repository.QueueMessageRepository;
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
    private QueueMessageRepository queueMessageRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            LaundryStateRepository laundryStateRepository,
                            QueueMessageRepository queueMessageRepository) {
        this.orderRepository = orderRepository;
        this.laundryStateRepository = laundryStateRepository;
        this.queueMessageRepository = queueMessageRepository;
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
        queueMessageRepository.save(orderSubmittedMessageEntity);
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

        updateQueueInfo(order, RequestType.COMPLETE);

        // TODO shine2 construct and publish OrderCompleteMessage(orEvent)
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
            }
        }
        this.completeOrder(orderEntity.getId());
    }

}
