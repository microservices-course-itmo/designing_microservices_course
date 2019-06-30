package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.OrderRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    //TODO
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LaundryStateService laundryStateService;

    @Autowired
    private PredictionService predictionService;

    @Override
    public OrderEntity updateOrderStatus(Integer orderId, OrderStatus orderStatus) {
        OrderEntity existingOrder = orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("Order with id = " + orderId + " was not found"));

        existingOrder.setStatus(orderStatus);
        return orderRepository.save(existingOrder);
    }

    @Override
    public OrderEntity getOrderById(Integer id) throws IllegalArgumentException {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with such id = " + id + " was not found"));
    }

    //TODO FormattedExceptions
    @Transactional
    @Override
    public OrderSubmissionDTO coordinateOrder(OrderDTO orderDto) {
        if (orderRepository.existsById(orderDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderDto.getOrderId() + "already exists");

        LaundryStateEntity leastLoadedLaundry = laundryStateService.getLeastLoadedLaundry();
        long estimatedCompletionTime = predictionService.getOrderCompletionTimePrediction(leastLoadedLaundry);

        OrderEntity orderEntity = new OrderEntity(orderDto, leastLoadedLaundry.getId(), estimatedCompletionTime);
        OrderSubmissionDTO orderSubmissionDTO = new OrderSubmissionDTO(orderEntity, leastLoadedLaundry);

        laundryStateService.updateLaundryStateByOrderSubmission(orderEntity);
        orderRepository.save(orderEntity);

        return orderSubmissionDTO;
    }

}
