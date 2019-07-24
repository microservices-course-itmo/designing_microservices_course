package com.microservices.taskcoordinator.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
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
    //TODO sukhoa: maybe rename to something starting with "EVENT"?

    private final Logger logger = LoggerFactory.getLogger(LaundryManagementEventConsumer.class);

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

        switch (laundryManagementEvent.getPayloadCase()) {
            case ORDERPROCESSEDEVENT: {
                OrderProcessedEvent orderProcessedEvent = laundryManagementEvent.getOrderProcessedEvent();
                logger.info("Received OrderProcessedEvent: {}", orderProcessedEvent);

                Span consumerSpan = createConsumerSideSpanFromMessage(laundryManagementEvent)
                        .name("consume_order_processed_event");
                consumerSpan.start();

                //TODO afanay: some kind of validation?
                OrderCompletedDto orderCompletedDto = new OrderCompletedDto(orderProcessedEvent);
                laundryStateService.updateLaundryStateWithOrderProcessed(orderCompletedDto);

                consumerSpan.finish();
                break;
            }
            case ORDERSUBMITTEDEVENT: {
                OrderSubmittedEvent orderSubmittedEvent = laundryManagementEvent.getOrderSubmittedEvent();
                logger.info("Received OrderSubmittedEvent: {}", orderSubmittedEvent);

                Span consumerSpan = createConsumerSideSpanFromMessage(laundryManagementEvent)
                        .name("consume_order_submitted_event");
                consumerSpan.start();

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
    }

    /**
     * Retrieve tracing headers set by brave such as trace id, span id and use them to
     * create new consumer side span and bind it to existing trace which was initiated earlier
     * on producer side.
     * <p>
     * This allows Zipkin to visualize the way of request along different services
     */
    private Span createConsumerSideSpanFromMessage(LaundryManagementEvent event) {
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
    public void setLaundryStateService(LaundryStateService laundryStateService) {
        this.laundryStateService = laundryStateService;
    }
}
