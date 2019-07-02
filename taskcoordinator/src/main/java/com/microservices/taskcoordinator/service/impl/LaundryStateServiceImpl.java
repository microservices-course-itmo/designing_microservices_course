package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDTO;
import com.microservices.taskcoordinator.dto.OrderDTO;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class LaundryStateServiceImpl implements LaundryStateService {

    private LaundryStateRepository laundryStateRepository;

    private OrderService orderService;

    @Override
    @Transactional
    public LaundryStateDTO updateLaundryStateWithOrderSubmission(int laundryId, long orderDuration) {
        if (laundryId < 0 || orderDuration < 0) {
            throw new IllegalArgumentException("laundryId or orderDuration can't be < 0");
        }

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(laundryId)
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + laundryId + "doesn't exist"));

        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() + orderDuration);

        laundryStateRepository.save(laundryStateEntity);
        return new LaundryStateDTO(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDTO updateLaundryStateWithOrderSubmitted(OrderSubmittedDTO orderSubmittedDTO) {
        Objects.requireNonNull(orderSubmittedDTO);

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderSubmittedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderSubmittedDTO.getOrderId() + "doesn't exist"));
        OrderDTO order = orderService.getOrderById(orderSubmittedDTO.getOrderId());

        if (laundryStateEntity.getVersion() < orderSubmittedDTO.getLaundryState().getVersion()) {
            laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() - order.getDuration());
            laundryStateEntity.setVersion(orderSubmittedDTO.getLaundryState().getVersion());
            laundryStateEntity.setQueueWaitingTime(orderSubmittedDTO.getLaundryState().getQueueWaitingTime());
        }

        order.setStatus(OrderStatus.SUBMITTED);

        orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        return new LaundryStateDTO(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateDTO updateLaundryStateWithOrderProcessed(OrderProcessedDTO orderProcessedDTO) {
        Objects.requireNonNull(orderProcessedDTO);

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderProcessedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderProcessedDTO.getOrderId() + "does not exist"));
        OrderDTO order = orderService.getOrderById(orderProcessedDTO.getOrderId());


        if (laundryStateEntity.getVersion() < orderProcessedDTO.getLaundryState().getVersion()) {
            laundryStateEntity.setVersion(orderProcessedDTO.getLaundryState().getVersion());
            laundryStateEntity.setQueueWaitingTime(orderProcessedDTO.getLaundryState().getQueueWaitingTime());
        }

        order.setStatus(OrderStatus.COMPLETE);
        order.setCompletionTime(System.currentTimeMillis());

        orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        return new LaundryStateDTO(laundryStateEntity);
    }

    @Override
    @Transactional
    public LaundryStateEntity getLeastLoadedLaundry() {
        return laundryStateRepository.getLeastLoadedLaundry();
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
