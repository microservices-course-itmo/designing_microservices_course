package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
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
    public OrderEntity updateOrder(OrderEntity orderEntity) {
        OrderEntity existingOrder = orderRepository.findById(orderEntity.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with id = " + orderEntity.getId()+ " was not found"));

        existingOrder.setStatus(orderEntity.getStatus());
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
    public OrderSubmissionDTO coordinateOrder(OrderCoordinationDTO orderCoordinationDto) {
        if (orderRepository.existsById(orderCoordinationDto.getOrderId()))
            throw new IllegalArgumentException("Order with id " + orderCoordinationDto.getOrderId() + "already exists");

        LaundryStateEntity leastLoadedLaundry = laundryStateService.getLeastLoadedLaundry();
        long estimatedCompletionTime = predictionService.getOrderCompletionTimePrediction(leastLoadedLaundry);

        OrderEntity orderEntity = new OrderEntity(orderCoordinationDto, leastLoadedLaundry.getId(), estimatedCompletionTime);

        orderRepository.save(orderEntity);
        laundryStateService.updateLaundryStateWithOrderSubmission(orderEntity.getId(), orderEntity.getDuration());

        return new OrderSubmissionDTO(orderEntity, leastLoadedLaundry);
    }

}
