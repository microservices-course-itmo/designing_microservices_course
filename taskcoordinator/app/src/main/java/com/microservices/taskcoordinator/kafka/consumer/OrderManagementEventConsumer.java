package com.microservices.taskcoordinator.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
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
    private final Logger logger = LoggerFactory.getLogger(OrderManagementEventConsumer.class);

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

        switch (orderManagementEvent.getPayloadCase()) {
            case ORDERCREATEDEVENT: {
                OrderCreatedEvent orderCreatedEvent = orderManagementEvent.getOrderCreatedEvent();
                logger.info("Received OrderCreatedEvent " + orderCreatedEvent);
                Span consumerSpan = createConsumerSideSpanFromMessage(orderManagementEvent)
                        .name("consume_order_processed_event");
                consumerSpan.start();

                // here deserializer processing
                orderService.coordinateOrder(new OrderCoordinationDto(orderCreatedEvent));

                consumerSpan.finish();
                break;
            }
            default: {
                // TODO Vlad : report this event to metric registry
                logger.info("Received unsupported event type: {}", orderManagementEvent.getPayloadCase());
            }
        }
    }

    /**
     * Retrieve tracing headers set by brave such as trace id, span id and use them to
     * create new consumer side span and bind it to existing trace which was initiated earlier
     * on producer side.
     * <p>
     * This allows Zipkin to visualize the way of request along different services
     */
    // TODO sukhoa: following code is duplicated in all consumers
    private Span createConsumerSideSpanFromMessage(OrderManagementEvent event) {
        TraceContext.Extractor<Object> extractor = tracing.propagation()
                .extractor((c, key) -> event.getPropertiesMap().get(key));

        return tracer.nextSpan(extractor.extract(event))
                .kind(CONSUMER);
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
