package com.microservices.ordermanagement.app.impl;

import com.microservices.ordermanagement.app.api.OrderService;
import com.microservices.ordermanagement.app.dto.AddDetailDto;
import com.microservices.ordermanagement.app.dto.AssignTariffDto;
import com.microservices.ordermanagement.app.entity.OrderEntity;
import com.microservices.ordermanagement.app.entity.PendingDetailEntity;
import com.microservices.ordermanagement.app.repository.OrderRepository;
import com.microservices.ordermanagement.app.repository.PendingDetailsRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
public class OrderServiceImpl implements OrderService {
    private final Logger logger = LoggerFactory.getLogger(PendingDetailServiceImpl.class);

    private OrderRepository orderRepository;

    private PendingDetailsRepository pendingDetailsRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, PendingDetailsRepository pendingDetailsRepository) {
        this.orderRepository = orderRepository;
        this.pendingDetailsRepository = pendingDetailsRepository;
    }

    @Override
    public OrderEntity getOrderById(int orderId) {
        logger.info("Looking for an order by id {}", orderId);
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalArgumentException("There is no such order in database"));
    }

    @Override
    @Transactional
    public OrderEntity addDetailToOrder(AddDetailDto addDetailDto) {
        Objects.requireNonNull(addDetailDto);

        PendingDetailEntity pendingDetail = pendingDetailsRepository.findById(addDetailDto.getPendingDetailId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "There is no detail with passed is found, id: " + addDetailDto.getPendingDetailId()));

        OrderEntity order;
        if (addDetailDto.getOrderId() == null) { // in case first detail. when order doesn't exist yet
            order = orderRepository.save(new OrderEntity());
            logger.info("Created new order with id: {}", order.getId());
        } else {
            order = this.getOrderById(addDetailDto.getOrderId());
        }
        order.addPendingDetail(pendingDetail);
        logger.info("Binding pending detail with order {}", addDetailDto);

        pendingDetailsRepository.deleteById(addDetailDto.getPendingDetailId());
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public OrderEntity assignTariffToOrderDetail(AssignTariffDto assignTariffDto) {
        Objects.requireNonNull(assignTariffDto);

        OrderEntity order = this.getOrderById(assignTariffDto.getOrderId());
        order.assignTariffToOrderDetail(assignTariffDto);
        logger.info("Assigning tariff to order detail {}", assignTariffDto);

        return orderRepository.save(order);
    }
}
