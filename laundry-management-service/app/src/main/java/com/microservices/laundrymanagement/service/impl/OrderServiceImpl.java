package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import com.microservices.laundrymanagement.repository.LaundryStateRepository;
import com.microservices.laundrymanagement.repository.OrderRepository;
import com.microservices.laundrymanagement.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private OrderRepository orderRepository;
    private LaundryStateRepository laundryStateRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Transactional
    @Override
    public void submitOrder(OrderSubmissionDto orderSubmissionDto) {
        OrderEntity orderEntity = new OrderEntity(orderSubmissionDto);
        orderRepository.save(orderEntity);

        int queueId = orderSubmissionDto.getLaundryId();
        LaundryStateEntity laundryState = laundryStateRepository.findById(queueId)
                .orElseThrow(IllegalArgumentException::new);// TODO shine2
        laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() + orderEntity.getEstimatedTime());
        laundryState.setVersion(laundryState.getVersion() + 1);
        LaundryStateEntity updatedLaundryState = laundryStateRepository.save(laundryState);

        // TODO shine2 construct and publish ORDER_SUBMITTED message
    }

    @Transactional
    @Override
    public void completeOrder(int id) {
        Optional<OrderEntity> orderOptional = orderRepository.findById(id); //todo: fix, logs, check
        if (!orderOptional.isPresent()) {
            throw new IllegalArgumentException();
        }
        OrderEntity order = orderOptional.get();
        order.setStatus(OrderStatus.COMPLETE);
        orderRepository.save(order);

        int queueId = order.getLaundryId();
        Optional<LaundryStateEntity> currentLaundryState = laundryStateRepository.findById(queueId);
        if (!currentLaundryState.isPresent())
            throw new IllegalArgumentException(); //что-то
        LaundryStateEntity laundryState = currentLaundryState.get();
        laundryState.setQueueWaitingTime(laundryState.getQueueWaitingTime() - order.getEstimatedTime());
        laundryStateRepository.save(laundryState);
    }
}
