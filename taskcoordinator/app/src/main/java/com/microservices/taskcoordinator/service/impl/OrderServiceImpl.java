package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.dto.LaundryStateDto;
import com.microservices.taskcoordinator.dto.OrderDto;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import com.microservices.taskcoordinator.repository.OrderRepository;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import com.microservices.taskcoordinator.service.TaskCoordinatorEventPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    private LaundryStateService laundryStateService;

    private TaskCoordinatorEventPublishingService taskCoordinatorEventPublishingService;

    @Override
    @Transactional
    public OrderDto updateOrder(OrderDto orderDTO) {
        Objects.requireNonNull(orderDTO);

        OrderEntity existingOrder = orderRepository.findById(orderDTO.getId())
                .orElseThrow(() -> new IllegalArgumentException("Order with id = " + orderDTO.getId()+ " was not found"));

        //TODO add formatted exceptions

        //nothing else can be updated
        existingOrder.setStatus(orderDTO.getStatus());
        existingOrder.setCompletionTime(orderDTO.getCompletionTime());
        OrderEntity orderSaved = orderRepository.save(existingOrder);

        return new OrderDto(orderSaved);
    }

    @Override
    @Transactional
    public OrderDto getOrderById(Integer id) throws IllegalArgumentException {
        Objects.requireNonNull(id);
        OrderEntity orderEntityFound = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Order with such id = " + id + " was not found"));

        return new OrderDto(orderEntityFound);
    }

    @Override
    @Transactional
    @NewSpan(name = "coordinate_order")
    public OrderSubmissionDto coordinateOrder(@SpanTag("order.coordination.dto") OrderCoordinationDto inboundOrder) {
        Objects.requireNonNull(inboundOrder);

        if (orderRepository.existsById(inboundOrder.getOrderId())) {
            throw new IllegalArgumentException("Order with id " + inboundOrder.getOrderId() + " already exists");
        }

        LaundryStateDto leastLoadedLaundry = laundryStateService.getLeastLoadedLaundry();
        long estimatedCompletionTime = laundryStateService.getCompletionTimePrediction(leastLoadedLaundry);

        OrderEntity orderEntity = new OrderEntity(inboundOrder, leastLoadedLaundry.getId(), estimatedCompletionTime);
        orderEntity.setStatus(OrderStatus.RESERVED);

        orderRepository.save(orderEntity);
        laundryStateService.updateLaundryStateWithOrderSubmission(orderEntity.getLaundryId(), orderEntity.getDuration());

        OrderSubmissionDto orderSubmissionDto = new OrderSubmissionDto(orderEntity);
        taskCoordinatorEventPublishingService.buildAndPublishOrderSubmissionEvent(orderSubmissionDto);

        return orderSubmissionDto;
    }

    @Autowired
    public void setOrderRepository(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Autowired
    public void setLaundryStateService(LaundryStateService laundryStateService) {
        this.laundryStateService = laundryStateService;
    }

    @Autowired
    public void setTaskCoordinatorEventPublishingService(TaskCoordinatorEventPublishingService taskCoordinatorEventPublishingService) {
        this.taskCoordinatorEventPublishingService = taskCoordinatorEventPublishingService;
    }
}
