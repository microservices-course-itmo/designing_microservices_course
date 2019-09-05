package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.InboundLaundryStateDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCompletedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.LaundryStateRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class LaundryStateServiceImpl implements LaundryStateService {

    private final Logger logger = LoggerFactory.getLogger(LaundryStateServiceImpl.class);

    private LaundryStateRepository laundryStateRepository;

    private OrderService orderService;

    private ModelMapper modelMapper;

    @Override
    public LaundryStateDto getLaundryStateById(int laundryId) {
        if (laundryId < 0) {
            throw new IllegalArgumentException("id can't be < 0");
        }

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(laundryId)
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + laundryId + "doesn't exist"));

        LaundryStateDto laundryStateDto = modelMapper.map(laundryStateEntity, LaundryStateDto.class);
        logger.info("Found laundryState: {}", laundryStateDto);

        return laundryStateDto;
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
        logger.info("Got laundryState to be updated with order-submission: {}", laundryStateEntity);

        laundryStateRepository.save(laundryStateEntity);

        LaundryStateDto laundryStateDto = modelMapper.map(laundryStateEntity, LaundryStateDto.class);
        logger.info("Updated laundry state: {}", laundryStateDto);

        return laundryStateDto;
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

        logger.info("Got laundryState to be updated with order-submitted: {}", laundryStateEntity);

        changeLaundryStateConsistently(orderSubmittedDTO.getLaundryState(), laundryStateEntity);
        laundryStateEntity.setReservedTime(laundryStateEntity.getReservedTime() - order.getDuration());

        order.setStatus(OrderStatus.SUBMITTED);

        OrderDto orderDto = orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        LaundryStateDto laundryStateDto = modelMapper.map(laundryStateEntity, LaundryStateDto.class);
        logger.info("Updated laundryState and order after order-submitted: {}, {}", laundryStateDto, orderDto);

        return laundryStateDto;
    }

    @Override
    @Transactional
    public LaundryStateDto updateLaundryStateWithOrderProcessed(OrderCompletedDto orderCompletedDTO) {
        Objects.requireNonNull(orderCompletedDTO);

        LaundryStateEntity laundryStateEntity = laundryStateRepository.findById(orderCompletedDTO.getLaundryState().getLaundryId())
                .orElseThrow(() -> new IllegalArgumentException("LaundryState with id " + orderCompletedDTO.getOrderId() + "does not exist"));
        OrderDto order = orderService.getOrderById(orderCompletedDTO.getOrderId());
        logger.info("Got laundryState to be updated with order-completed: {}", laundryStateEntity);

        if (order.getStatus() != OrderStatus.SUBMITTED) {
            throw new IllegalArgumentException("Order must be submitted before completion");
        }

        changeLaundryStateConsistently(orderCompletedDTO.getLaundryState(), laundryStateEntity);

        order.setStatus(OrderStatus.COMPLETED);
        order.setCompletionTime(orderCompletedDTO.getCompletionTime());

        OrderDto orderDto = orderService.updateOrder(order);
        laundryStateRepository.save(laundryStateEntity);

        LaundryStateDto laundryStateDto = modelMapper.map(laundryStateEntity, LaundryStateDto.class);
        logger.info("Updated laundryState and order after order-completed: {}, {}", laundryStateDto, orderDto);

        return laundryStateDto;
    }

    @Override
    @Transactional
    public LaundryStateDto getLeastLoadedLaundry() {
        LaundryStateEntity foundStateEntity = laundryStateRepository.getLeastLoadedLaundries()
                .stream()
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("there are no laundries to process the order"));

        return modelMapper.map(foundStateEntity, LaundryStateDto.class);
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

    @Autowired
    public void setModelMapper(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }
}
