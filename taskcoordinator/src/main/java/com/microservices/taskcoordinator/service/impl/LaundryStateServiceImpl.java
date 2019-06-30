package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDTO;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LaundryStateServiceImpl implements LaundryStateService {

    private final LaundryStateRepository laundryStateRepository;

    private final OrderService orderService;

    @Autowired
    public LaundryStateServiceImpl(LaundryStateRepository laundryStateRepository, OrderService orderService) {
        this.laundryStateRepository = laundryStateRepository;
        this.orderService = orderService;
    }

    @Override
    public LaundryStateDTO updateLaundryStateByOrderSubmission(OrderEntity orderEntity) {
        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderEntity.getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderEntity.getLaundryId() + "doesn't exist"));

        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() + orderEntity.getDuration());

        laundryStateRepository.save(laundryStateEntity);
        return new LaundryStateDTO(laundryStateEntity);
    }

    @Override
    public LaundryStateEntity updateLaundryStateOrderSubmitted(OrderSubmittedDTO orderSubmittedDTO) {
        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderSubmittedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderSubmittedDTO.getOrderId() + "doesn't exist"));
        OrderEntity orderEntity = orderService.getOrderById(orderSubmittedDTO.getOrderId());

        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() - orderEntity.getDuration());
        laundryStateEntity.setQueueWaitingTime(orderSubmittedDTO.getLaundryState().getQueueWaitingTime());

        if (laundryStateEntity.getVersion() < orderSubmittedDTO.getLaundryState().getVersion()) {
            laundryStateEntity.setVersion(orderSubmittedDTO.getLaundryState().getVersion());
        }

        orderService.updateOrderStatus(orderEntity.getId(), OrderStatus.SUBMITTED);

        return laundryStateRepository.save(laundryStateEntity);
    }

    @Override
    public LaundryStateEntity updateLaundryStateOrderProcessed(OrderProcessedDTO orderProcessedDTO) {
        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderProcessedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderProcessedDTO.getOrderId() + "doesn't exist"));
        OrderEntity orderEntity = orderService.getOrderById(orderProcessedDTO.getOrderId());

        laundryStateEntity.setQueueWaitingTime(orderProcessedDTO.getLaundryState().getQueueWaitingTime());

        if (laundryStateEntity.getVersion() < orderProcessedDTO.getLaundryState().getVersion()) {
            laundryStateEntity.setVersion(orderProcessedDTO.getLaundryState().getVersion());
        }

        orderService.updateOrderStatus(orderEntity.getId(), OrderStatus.COMPLETE);

        return laundryStateRepository.save(laundryStateEntity);
    }

    @Override
    public LaundryStateEntity getLeastLoadedLaundry() {
        return laundryStateRepository.getLeastLoadedLaundry();
    }
}
