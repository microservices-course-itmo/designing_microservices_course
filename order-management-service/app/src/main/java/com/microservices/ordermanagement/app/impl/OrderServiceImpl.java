package com.microservices.ordermanagement.app.impl;

import com.microservices.ordermanagement.api.dto.AddDetailDto;
import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.dto.OrderStatus;
import com.microservices.ordermanagement.api.dto.PaymentDetailsDto;
import com.microservices.ordermanagement.app.api.OrderManagementEventPublishingService;
import com.microservices.ordermanagement.app.api.OrderService;
import com.microservices.ordermanagement.app.entity.OrderEntity;
import com.microservices.ordermanagement.app.entity.PendingDetailEntity;
import com.microservices.ordermanagement.app.repository.OrderRepository;
import com.microservices.ordermanagement.app.repository.PendingDetailsRepository;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.Objects;
import java.util.Set;

@Service
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(PendingDetailServiceImpl.class);

    private OrderRepository orderRepository;

    private PendingDetailsRepository pendingDetailsRepository;

    /**
     * Object for publishing events to database event table
     */
    private OrderManagementEventPublishingService orderManagementEventPublishingService;

    /**
     * Objects for mapping DTOs to entities and vise versa
     */
    private ModelMapper modelMapper;

    /**
     * Javax calidator preconfigured by Spring Boot.
     */
    private Validator validator;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PendingDetailsRepository pendingDetailsRepository, OrderManagementEventPublishingService orderManagementEventPublishingService, ModelMapper modelMapper, Validator validator) {
        this.orderRepository = orderRepository;
        this.pendingDetailsRepository = pendingDetailsRepository;
        this.orderManagementEventPublishingService = orderManagementEventPublishingService;
        this.modelMapper = modelMapper;
        this.validator = validator;
    }

    @Override
    public OrderEntity getOrderById(int orderId) {
        logger.info("Looking for an order by id {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("There is no such order in database"));
    }

    @Override
    @Transactional
    public OrderDto addDetailToOrder(AddDetailDto addDetailDto) {
        Objects.requireNonNull(addDetailDto);

        PendingDetailEntity pendingDetail = pendingDetailsRepository.findById(addDetailDto.getPendingDetailId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "There is no detail with passed is found, id: " + addDetailDto.getPendingDetailId()));

        OrderEntity order;
        if (addDetailDto.getOrderId() == null) { // in case first detail. when order doesn't exist yet
            order = orderRepository.save(new OrderEntity(addDetailDto.getUsername()));
            logger.info("Created new order with id: {}", order.getId());
        } else {
            order = this.getOrderById(addDetailDto.getOrderId());
        }
        order.addDetail(pendingDetail);
        logger.info("Binding pending detail with order {}", addDetailDto);

        pendingDetailsRepository.deleteById(addDetailDto.getPendingDetailId());
        OrderEntity savedOrder = orderRepository.save(order);

        return mapToDtoAndValidate(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto assignTariffToOrderDetail(AssignTariffDto assignTariffDto) {
        Objects.requireNonNull(assignTariffDto);

        OrderEntity order = this.getOrderById(assignTariffDto.getOrderId());
        order.assignTariffToOrderDetail(assignTariffDto);
        logger.info("Assigning tariff to order detail {}", assignTariffDto);

        OrderEntity savedOrder = orderRepository.save(order);
        return mapToDtoAndValidate(savedOrder);
    }

    @Override
    @Transactional
    public OrderDto changeOrderStatus(int orderId, OrderStatus newStatus) {
        Objects.requireNonNull(newStatus);
        logger.info("Attempt to change order status, order id : {}, new status {}", orderId, newStatus);

        OrderEntity order = this.getOrderById(orderId);
        OrderStatus previousStatus = order.getStatus();
        switch (newStatus) {
            case SUBMITTED: {
                if (order.getStatus() == OrderStatus.CREATED) {
                    order.setStatus(newStatus);
                } else {
                    throw new IllegalStateException("Not allowed status transition from " +
                            newStatus + " to " + order.getStatus());
                }
                break;
            }
            case COMPLETE: {
                if (order.getStatus() == OrderStatus.SUBMITTED) {
                    order.setStatus(newStatus);
                } else {
                    throw new IllegalStateException("Not allowed status transition from " +
                            newStatus + " to " + order.getStatus());
                }
                break;
            }
            case FAILED: {
                // TODO sukhoa handle this in proper way
                throw new UnsupportedOperationException("Not implemented FAILED status setting operation");
            }
            default: {
                throw new IllegalArgumentException("Not supported status setting operation for: " + newStatus);
            }
        }


        OrderEntity savedOrder = orderRepository.save(order);
        OrderDto orderDto = mapToDtoAndValidate(savedOrder);

        logger.info("Order status changed, order id: {}, previous status: {}, current status: {}",
                orderId, previousStatus, newStatus);

        return orderDto;
    }

    @Override
    @Transactional
    public OrderDto checkValidAndApprove(int orderId, PaymentDetailsDto paymentDetailsDto) {
        Objects.requireNonNull(paymentDetailsDto);

        logger.info("Attempt approve order id {}, payment details {}", orderId, paymentDetailsDto);

        OrderEntity order = this.getOrderById(orderId);
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new IllegalStateException("Order already not in PENDING status and cannot be approved again");
        }

        if (!order.getTotalPrice().equals(paymentDetailsDto.getAmount())) {
            throw new IllegalStateException("Order price: " + order.getTotalPrice() +
                    " . Doesn't match payment amount: " + paymentDetailsDto.getAmount());
        }

        // TODO sukhoa: check username

        order.setStatus(OrderStatus.CREATED);
        OrderEntity savedOrder = orderRepository.save(order);

        orderManagementEventPublishingService.buildAndPublishOrderCreatedEvent(savedOrder);

        OrderDto orderDto = mapToDtoAndValidate(savedOrder);

        logger.info("Approved order id: {}", orderId);
        return orderDto;
    }

    private OrderDto mapToDtoAndValidate(OrderEntity order) {
        OrderDto orderDto = modelMapper.map(order, OrderDto.class);
        Set<ConstraintViolation<OrderDto>> violations = validator.validate(orderDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
        return orderDto;
    }
}
