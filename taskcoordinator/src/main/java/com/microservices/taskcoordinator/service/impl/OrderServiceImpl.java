package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
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

    //TODO FormattedExceptions
    @Transactional
    @Override
    public OrderSubmissionDTO coordinateOrder(OrderDTO orderDto) {
        if (orderRepository.existsById(orderDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderDto.getOrderId() + "already exists");

        LaundryStateEntity leastLoadedLaundry = laundryStateService.getLeastLoadedLaundry();
        long estimatedCompletionTime = predictionService.getOrderCompletionTimePrediction(leastLoadedLaundry);

        /*OrderEntity orderEntity = new OrderEntity(orderDto, leastLoadedLaundry.getId(), estimatedCompletionTime);

        OrderSubmissionDTO orderSubmissionDTO = new OrderSubmissionDTO()

        laundryStateService.updateLaundryStateOrderSubmission()

        orderRepository.save(orderEntity);*/

        return null;
    }

}
