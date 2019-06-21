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
        // TODO shine2 check if It already exists
        orderRepository.save(orderEntity);

        LaundryStateEntity laundryStateEntity = updateQueueInfo(orderEntity, RequestType.SUBMIT);

        // TODO shine1 come up with the graceful way how not to save duplicates (actually It is not a big deal but..)
        OrderSubmittedMessageEntity orderSubmittedMessageEntity = new OrderSubmittedMessageEntity(
                orderEntity.getId(), laundryStateEntity);
        queueMessageRepository.save(orderSubmittedMessageEntity);
    }

    @Transactional
    @Override
    public void completeOrder(int id) {
        OrderEntity order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with id " + id + " is not found")); //todo: fix, logs, check
        // TODO shine2 handle case of repeatedly completing orders
        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        updateQueueInfo(order, RequestType.COMPLETE); // TODO shine2 handle duplicates

        // TODO shine2 construct and publish OrderCompleteMessage(orEvent)
    }

    private LaundryStateEntity updateQueueInfo(OrderEntity order, RequestType requestType) {
        int queueId = order.getLaundryId();
        LaundryStateEntity laundryState = laundryStateRepository.findById(queueId)
                .orElseThrow(() -> new IllegalArgumentException("Queue with id" + queueId + "is not found"));

        switch (requestType) {
            case SUBMIT:
                laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() + order.getEstimatedTime());
                break;
            case COMPLETE:
                laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() - order.getEstimatedTime());
                break;
        }

//        laundryState.setVersion(laundryState.getVersion() + 1);
        return laundryStateRepository.save(laundryState);
    }

    @Transactional
    public void completeNextOrderInQueue(int laundryId) {
        Optional<OrderEntity> nextOrderInQueue = orderRepository.findNextOrderInQueue(laundryId);
        if (!nextOrderInQueue.isPresent()) {
            // TODO shine2 log this fact and get rid of warning
        }
        this.completeOrder(nextOrderInQueue.get().getId()); // TODO get rid of warning
    }

}
