package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.InboundLaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
public class LaundryStateServiceImpl implements LaundryStateService {

    private LaundryStateRepository laundryStateRepository;

    private OrderService orderService;

    @Override
    public LaundryStateDto getLaundryStateById(int laundryId) {
        if (laundryId < 0) {
            throw new IllegalArgumentException("id can't be < 0");
        }

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(laundryId)
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + laundryId + "doesn't exist"));

        return new LaundryStateDto(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDto updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration) {
        if (laundryId < 0 || orderDuration < 0) {
            throw new IllegalArgumentException("id or orderDuration can't be < 0");
        }

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(laundryId)
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + laundryId + "doesn't exist"));

        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() + orderDuration);

        laundryStateRepository.save(laundryStateEntity);
        return new LaundryStateDto(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDto updateLaundryStateWithOrderSubmitted(OrderSubmittedDto orderSubmittedDTO) {
        Objects.requireNonNull(orderSubmittedDTO);

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderSubmittedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderSubmittedDTO.getOrderId() + "doesn't exist"));
        OrderDto order = orderService.getOrderById(orderSubmittedDTO.getOrderId());

        if (order.getStatus() != OrderStatus.RESERVED) {
            throw new IllegalArgumentException("Order must be reserved before submitting");
        }

        changeLaundryStateConsistently(orderSubmittedDTO.getLaundryState(), laundryStateEntity);
        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() - order.getDuration());

        order.setStatus(OrderStatus.SUBMITTED);

        orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        return new LaundryStateDto(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDto updateLaundryStateWithOrderProcessed(OrderProcessedDto orderProcessedDTO) {
        Objects.requireNonNull(orderProcessedDTO);

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderProcessedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderProcessedDTO.getOrderId() + "does not exist"));
        OrderDto order = orderService.getOrderById(orderProcessedDTO.getOrderId());

        if (order.getStatus() != OrderStatus.SUBMITTED) {
            throw new IllegalArgumentException("Order must be submitted before completion");
        }

        changeLaundryStateConsistently(orderProcessedDTO.getLaundryState(), laundryStateEntity);

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletionTime(orderProcessedDTO.getCompletionTime());

        orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        return new LaundryStateDto(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDto getLeastLoadedLaundry() {
        return new LaundryStateDto(laundryStateRepository.getLeastLoadedLaundries()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("there are no laundries to process the order")));
    }

    @Override
    public long getCompletionTimePrediction(LaundryStateDto laundryStateDto) {
        return laundryStateDto.getQueueWaitingTime() + laundryStateDto.getReservedTime();
    }

    private void changeLaundryStateConsistently(InboundLaundryStateDto inboundLaundryStateUpdate, LaundryStateEntity currentLaundryState) {
        if (currentLaundryState.getVersion() < inboundLaundryStateUpdate.getVersion()) {
            currentLaundryState.setVersion(inboundLaundryStateUpdate.getVersion());
            currentLaundryState.setQueueWaitingTime(inboundLaundryStateUpdate.getQueueWaitingTime());
        }
    }

    @Autowired
    public void setLaundryStateRepository(LaundryStateRepository laundryStateRepository) {
        this.laundryStateRepository = laundryStateRepository;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
