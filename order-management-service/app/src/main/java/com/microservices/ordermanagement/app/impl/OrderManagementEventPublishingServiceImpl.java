package com.microservices.ordermanagement.app.impl;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.microservices.ordermanagement.api.events.OrderCreatedEventWrapper.OrderCreatedEvent;
import com.microservices.ordermanagement.api.events.OrderDetailWrapper.OrderDetail;
import com.microservices.ordermanagement.api.events.OrderManagementEventWrapper.OrderManagementEvent;
import com.microservices.ordermanagement.api.events.OrderWrapper.Order;
import com.microservices.ordermanagement.app.api.OrderManagementEventPublishingService;
import com.microservices.ordermanagement.app.entity.EventType;
import com.microservices.ordermanagement.app.entity.OrderEntity;
import com.microservices.ordermanagement.app.entity.OrderManagementEventLogEntity;
import com.microservices.ordermanagement.app.repository.OrderManagementEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static brave.Span.Kind.PRODUCER;

@Service
public class OrderManagementEventPublishingServiceImpl implements OrderManagementEventPublishingService {

    private OrderManagementEventRepository eventRepository;

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;


    @Autowired
    public OrderManagementEventPublishingServiceImpl(OrderManagementEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Override
    // should be always performed in current transaction if exists to support transactional messaging pattern
    @Transactional(propagation = Propagation.REQUIRED)
    @NewSpan(name = "publish_order_created_event")
    public void buildAndPublishOrderCreatedEvent(OrderEntity order) {
        Span oneWaySend = tracer.currentSpan() // might return null
                .kind(PRODUCER)
                .start();

        List<OrderDetail> orderDetails = order.getDetailEntities().stream()
                .map(d -> OrderDetail.newBuilder()
                        .setDetailId(d.getId())
                        .setOrderId(d.getOrderId())
                        .setDuration(d.getDuration())
                        .setWeight(d.getWeight())
                        .build())
                .collect(Collectors.toList());

        Order orderEvent = Order.newBuilder()
                .setOrderId(order.getId())
                .addAllDetails(orderDetails)
                .build();

        OrderCreatedEvent orderCreatedEvent = OrderCreatedEvent.newBuilder()
                .setOrder(orderEvent)
                .build();

        OrderManagementEvent orderManagementEvent = OrderManagementEvent.newBuilder()
                .setOrderCreatedEvent(orderCreatedEvent)
                .putAllProperties(createTracingPropertiesToDistribute(oneWaySend))
                .build();

        OrderManagementEventLogEntity event = new OrderManagementEventLogEntity(
                EventType.ORDER_CREATED_EVENT, orderManagementEvent.toByteArray());

        eventRepository.save(event);
        oneWaySend.finish();

    }

    /**
     * Result map contains headers (trace id, span id and so on) which will be propagated
     * in deserializer in order for receiving side to be able to attach further actions to current trace TODO sukhoa rewrite doc
     */
    private Map<String, String> createTracingPropertiesToDistribute(Span span) {
        Map<String, String> tracingInformation = new HashMap<>();
        tracing.propagation()
                .injector((carrier, key, val) -> tracingInformation.put(key, val))
                .inject(span.context(), tracingInformation);
        return tracingInformation;
    }
}
