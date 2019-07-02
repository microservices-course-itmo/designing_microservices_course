package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.OrderDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.repository.OrderRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final LaundryStateService laundryStateService;

    private final PredictionService predictionService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, LaundryStateService laundryStateService, PredictionService predictionService) {
        this.orderRepository = orderRepository;
        this.laundryStateService = laundryStateService;
        this.predictionService = predictionService;
    }

    @Override
    @Transactional
    public OrderDTO updateOrder(OrderDTO orderDTO) {
        OrderEntity existingOrder = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with id = " + orderDTO.getId()+ " was not found"));

        //TODO add formatted exceptions

        //nothing else can be updated
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setCompletionTime(orderDTO.getCompletionTime());
        OrderEntity orderSaved = orderRepository.save(existingOrder);

        return new OrderDTO(orderSaved);
    }

    @Override
    @Transactional
    public OrderDTO getOrderById(Integer id) throws IllegalArgumentException {
        OrderEntity orderEntityFound = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with such id = " + id + " was not found"));

        return new OrderDTO(orderEntityFound);
    }

    @Override
    @Transactional
    public OrderSubmissionDTO coordinateOrder(OrderCoordinationDTO inboundOrder) {
        if (orderRepository.existsById(inboundOrder.getOrderId()))
            throw new IllegalArgumentException("Order with id " + inboundOrder.getOrderId() + "already exists");

        LaundryStateEntity leastLoadedLaundry = laundryStateService.getLeastLoadedLaundry();
        long estimatedCompletionTime = predictionService.getOrderCompletionTimePrediction(leastLoadedLaundry);

        OrderEntity orderEntity = new OrderEntity(inboundOrder, leastLoadedLaundry.getId(), estimatedCompletionTime);

        orderRepository.save(orderEntity);
        laundryStateService.updateLaundryStateWithOrderSubmission(orderEntity.getId(), orderEntity.getDuration());

        return new OrderSubmissionDTO(orderEntity);
    }
}
