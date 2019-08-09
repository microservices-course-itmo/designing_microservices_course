package com.microservices.taskcoordinator.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import com.microservices.laundrymanagement.api.messages.OrderProcessedEventWrapper.OrderProcessedEvent;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper.OrderSubmittedEvent;
import com.microservices.taskcoordinator.dto.inbound.OrderCompletedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.service.LaundryStateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static brave.Span.Kind.CONSUMER;

@Component
public class LaundryManagementEventConsumer {
    private static final Logger logger = LoggerFactory.getLogger(LaundryManagementEventConsumer.class);

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;

    private LaundryStateService laundryStateService;

    @KafkaListener(
            topics = "${laundry.management.topic.name}",
            groupId = "${laundry.management.listener.name}",
            containerFactory = "laundryManagementListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(LaundryManagementEvent laundryManagementEvent) {
        try (CurrentTraceContext.Scope scope = initNewScopeFromExtractedTraceInfo(laundryManagementEvent)) {
            Span consumerSpan = tracer.nextSpan()
                    .kind(CONSUMER)
                    .start();

            switch (laundryManagementEvent.getPayloadCase()) {
                case ORDERPROCESSEDEVENT: {
                    OrderProcessedEvent orderProcessedEvent = laundryManagementEvent.getOrderProcessedEvent();
                    logger.info("Received OrderProcessedEvent " + orderProcessedEvent);
                    consumerSpan
                            .customizer()
                            .name("consume_order_processed_event");

                    //TODO afanay: some kind of validation?
                    OrderProcessedDto orderProcessedDto = new OrderProcessedDto(orderProcessedEvent);
                    laundryStateService.updateLaundryStateWithOrderProcessed(orderProcessedDto);

                    consumerSpan.finish();
                    break;
                }
                case ORDERSUBMITTEDEVENT: {
                    OrderSubmittedEvent orderSubmittedEvent = laundryManagementEvent.getOrderSubmittedEvent();
                    logger.info("Received OrderSubmittedEvent" + orderSubmittedEvent);

                    consumerSpan
                            .customizer()
                            .name("consume_order_submitted_event");

                    // here deserializer processing
                    OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(orderSubmittedEvent);
                    laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDto);

                    consumerSpan.finish();
                    break;
                }
                default: {
                    // TODO Vlad : report this event to metric registry
                    logger.info("Received unsupported event type: {}", laundryManagementEvent.getPayloadCase());
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
    private CurrentTraceContext.Scope initNewScopeFromExtractedTraceInfo(LaundryManagementEvent event) {
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
    public void setLaundryStateService(LaundryStateService laundryStateService) {
        this.laundryStateService = laundryStateService;
    }
}
