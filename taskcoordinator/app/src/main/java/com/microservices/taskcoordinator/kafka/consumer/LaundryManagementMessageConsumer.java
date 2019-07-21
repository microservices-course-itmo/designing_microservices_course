package com.microservices.taskcoordinator.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.TraceContext;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import com.microservices.laundrymanagement.api.messages.OrderProcessedEventWrapper.OrderProcessedEvent;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper.OrderSubmittedEvent;
import com.microservices.taskcoordinator.dto.inbound.OrderProcessedDto;
import com.microservices.taskcoordinator.dto.inbound.OrderSubmittedDto;
import com.microservices.taskcoordinator.service.LaundryStateService;
import com.microservices.taskcoordinator.service.OrderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static brave.Span.Kind.CONSUMER;

@Component
public class LaundryManagementMessageConsumer {
    //TODO sukhoa: maybe rename to something starting with "EVENT"?

    private final Logger logger = LoggerFactory.getLogger(LaundryManagementMessageConsumer.class);

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;

    private OrderService orderService;

    private LaundryStateService laundryStateService;

    @KafkaListener(
            topics = "${laundry.management.topic.name}",
            groupId = "${laundry.management.listener.name}",
            containerFactory = "laundryManagementListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(LaundryManagementEvent message) {

        switch (message.getPayloadCase()) {
            case ORDERPROCESSEDEVENT: {
                OrderProcessedEvent event = message.getOrderProcessedEvent();
                logger.info("Received OrderProcessedEvent " + event);
                Span consumerSpan = createConsumerSideSpanFromMessage(message)
                        .name("consume_order_processed_event");
                consumerSpan.start();

                // here message processing
                //TODO afanay: some kind of validation?
                OrderProcessedDto orderProcessedDto = new OrderProcessedDto(event);
                laundryStateService.updateLaundryStateWithOrderProcessed(orderProcessedDto);

                consumerSpan.finish();
                break;
            }
            case ORDERSUBMITTEDEVENT: {
                OrderSubmittedEvent event = message.getOrderSubmittedEvent();
                logger.info("Received OrderSubmittedEvent" + event);
                Span consumerSpan = createConsumerSideSpanFromMessage(message)
                        .name("consume_order_submitted_event");
                consumerSpan.start();

                // here message processing
                OrderSubmittedDto orderSubmittedDto = new OrderSubmittedDto(event);
                laundryStateService.updateLaundryStateWithOrderSubmitted(orderSubmittedDto);

                consumerSpan.finish();
                break;
            }
            default: {
                // TODO Vlad : report this event to metric registry
                logger.info("Received unsupported event type: {}", message.getPayloadCase());
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
    private Span createConsumerSideSpanFromMessage(LaundryManagementEvent message) {
        TraceContext.Extractor<Object> extractor = tracing.propagation()
                .extractor((c, key) -> message.getPropertiesMap().get(key));

        return tracer.nextSpan(extractor.extract(message))
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

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
