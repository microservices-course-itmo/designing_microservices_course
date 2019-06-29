package com.microservices.ordermanagement.app.impl;

import com.microservices.ordermanagement.app.api.OrderService;
import com.microservices.ordermanagement.app.dto.AddDetailDto;
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
import java.util.Optional;

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
    @Transactional
    public OrderEntity addDetailToOrder(AddDetailDto addDetailDto) {
        Objects.requireNonNull(addDetailDto);
        if (addDetailDto.getPendingDetailId() == null) {
            throw new IllegalArgumentException("Passed pending detail id is null");
        }

        Optional<PendingDetailEntity> pendingDetail = pendingDetailsRepository.findById(
                Optional.ofNullable(addDetailDto.getPendingDetailId())
                        .orElseThrow(() -> new IllegalArgumentException("Passed detail id in null")));

        if (!pendingDetail.isPresent()) {
            throw new IllegalArgumentException(
                    "There is no detail with passed is found, id: " + addDetailDto.getPendingDetailId());
        }

        OrderEntity order;
        if (addDetailDto.getOrderId() == null) {
            order = orderRepository.save(new OrderEntity());
        } else {
            order = orderRepository.findById(addDetailDto.getOrderId())
                    .orElseThrow(() -> new IllegalArgumentException("Passed detail id in null"));
        }
        order.addPendingDetail(pendingDetail.get());

        pendingDetailsRepository.deleteById(addDetailDto.getPendingDetailId());

        return orderRepository.save(order);
    }
}
