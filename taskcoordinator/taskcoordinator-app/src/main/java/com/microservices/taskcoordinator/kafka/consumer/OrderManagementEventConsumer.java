package com.microservices.taskcoordinator.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.microservices.ordermanagement.api.events.OrderCreatedEventWrapper.OrderCreatedEvent;
import com.microservices.ordermanagement.api.events.OrderManagementEventWrapper.OrderManagementEvent;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDto;
import com.microservices.taskcoordinator.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static brave.Span.Kind.CONSUMER;

@Component
public class OrderManagementEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(OrderManagementEventConsumer.class);

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;

    private OrderService orderService;

    @KafkaListener(
            topics = "${order.management.topic.name}",
            groupId = "${order.management.listener.name}",
            containerFactory = "orderManagementListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(OrderManagementEvent orderManagementEvent) {

        try (CurrentTraceContext.Scope scope = initNewScopeFromExtractedTraceInfo(orderManagementEvent)) {
            Span consumerSpan = tracer.nextSpan()
                    .kind(CONSUMER)
                    .start();

            switch (orderManagementEvent.getPayloadCase()) {
                case ORDERCREATEDEVENT: {
                    OrderCreatedEvent orderCreatedEvent = orderManagementEvent.getOrderCreatedEvent();

                    consumerSpan
                            .customizer()
                            .name("consume_order_created_event");

                    logger.info("Received OrderCreatedEvent " + orderCreatedEvent);
                    orderService.coordinateOrder(new OrderCoordinationDto(orderCreatedEvent));
                    break;
                }
                default: {
                    // TODO Vlad : report this event to metric registry
                    logger.info("Received unsupported event type: {}", orderManagementEvent.getPayloadCase());
                }
            }
            consumerSpan.finish();
        }
    }

    /**
     * Retrieve tracing headers set by brave such as trace id, span id and use them to
     * propagate tracing context from producer to consumer side.
     * <p>
     * I did not find another way how to propagate existing context and place all
     * subsequent spans under it. Ones we figure out how to do it automatically or
     * just in more elegant way this code should be replaced
     * <p>
     * This allows Zipkin to visualize the way of request along different services
     *
     * @return new scope which should be closed
     */
    // TODO sukhoa: following code is duplicated in all consumers
    private CurrentTraceContext.Scope initNewScopeFromExtractedTraceInfo(OrderManagementEvent event) {
        TraceContext.Extractor<Object> extractor = tracing.propagation()
                .extractor((c, key) -> event.getPropertiesMap().get(key));
        return tracing.currentTraceContext().newScope(extractor.extract(event).context());
    }

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }

    @Autowired
    public void setTracing(Tracing tracing) {
        this.tracing = tracing;
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
