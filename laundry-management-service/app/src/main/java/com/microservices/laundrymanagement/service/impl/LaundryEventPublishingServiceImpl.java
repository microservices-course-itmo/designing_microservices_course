package com.microservices.laundrymanagement.service.impl;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper;
import com.microservices.laundrymanagement.api.messages.LaundryStateWrapper;
import com.microservices.laundrymanagement.api.messages.OrderProcessedEventWrapper.OrderProcessedEvent;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper;
import com.microservices.laundrymanagement.entity.EventType;
import com.microservices.laundrymanagement.entity.LaundryEventLogEntity;
import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import com.microservices.laundrymanagement.repository.LaundryEventRepository;
import com.microservices.laundrymanagement.service.LaundryEventPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.sleuth.annotation.NewSpan;
import org.springframework.cloud.sleuth.annotation.SpanTag;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

import static brave.Span.Kind.PRODUCER;

@Service
public class LaundryEventPublishingServiceImpl implements LaundryEventPublishingService {

    private LaundryEventRepository eventRepository;

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;

    @Autowired
    public LaundryEventPublishingServiceImpl(LaundryEventRepository eventRepository, Tracer tracer, Tracing tracing) {
        this.eventRepository = eventRepository;
        this.tracer = tracer;
        this.tracing = tracing;
    }

    @Override
    // should be always performed in current transaction if exists to support transactional messaging pattern
    @Transactional(propagation = Propagation.REQUIRED)
    @NewSpan(name = "publish_order_submitted_event")
    public void buildAndPublishOrderSubmittedEvent(@SpanTag(key = "order.id") int orderId, LaundryStateEntity laundryState) {
        Span oneWaySend = tracer.currentSpan() // might return null
                .kind(PRODUCER)
                .start();

        OrderSubmittedEventWrapper.OrderSubmittedEvent orderSubmittedEvent = OrderSubmittedEventWrapper
                .OrderSubmittedEvent.newBuilder()
                .setOrderId(orderId)
                .setState(LaundryStateWrapper.LaundryState.newBuilder()
                        .setLaundryId(laundryState.getId())
                        .setQueueWaitingTime(laundryState.getQueueWaitingTime())
                        .setVersion(laundryState.getVersion()))
                .build();

        LaundryManagementEventWrapper.LaundryManagementEvent laundryManagementEvent = LaundryManagementEventWrapper
                .LaundryManagementEvent.newBuilder()
                .setOrderSubmittedEvent(orderSubmittedEvent)
                .putAllProperties(createTracingPropertiesToDistribute(oneWaySend))
                .build();

        LaundryEventLogEntity event = new LaundryEventLogEntity(
                EventType.ORDER_SUBMITTED_EVENT, laundryManagementEvent.toByteArray());

        eventRepository.save(event);
        oneWaySend.finish();
    }

    @Override
    // should be always performed in current transaction if exists to support transactional messaging pattern
    @Transactional(propagation = Propagation.REQUIRED)
    @NewSpan(name = "publish_order_processed_event")
    public void buildAndPublishOrderProcessedEvent(@SpanTag(key = "order.id") int orderId, LaundryStateEntity laundryState) {
        Span oneWaySend = tracer.currentSpan() // might return null
                .kind(PRODUCER)
                .start();

        OrderProcessedEvent orderProcessedEvent = OrderProcessedEvent.newBuilder()
                .setOrderId(orderId)
                .setCompleteTime(System.currentTimeMillis())
                .setState(LaundryStateWrapper.LaundryState.newBuilder()
                        .setLaundryId(laundryState.getId())
                        .setQueueWaitingTime(laundryState.getQueueWaitingTime())
                        .setVersion(laundryState.getVersion()))
                .build();

        LaundryManagementEventWrapper.LaundryManagementEvent laundryManagementEvent = LaundryManagementEventWrapper
                .LaundryManagementEvent.newBuilder()
                .putAllProperties(createTracingPropertiesToDistribute(oneWaySend))
                .setOrderProcessedEvent(orderProcessedEvent).build();

        LaundryEventLogEntity event = new LaundryEventLogEntity(
                EventType.ORDER_COMPLETED_EVENT, laundryManagementEvent.toByteArray());

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
