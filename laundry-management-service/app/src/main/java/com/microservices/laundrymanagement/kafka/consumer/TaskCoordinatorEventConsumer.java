package com.microservices.laundrymanagement.kafka.consumer;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import brave.propagation.CurrentTraceContext;
import brave.propagation.TraceContext;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.OrderService;
import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper.TaskCoordinatorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import static brave.Span.Kind.CONSUMER;

@Component
public class TaskCoordinatorEventConsumer {

    private static final Logger logger = LoggerFactory.getLogger(TaskCoordinatorEventConsumer.class);

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
            topics = "${taskcoordinator.topic.name}",
            groupId = "${taskcoordinator.listener.name}",
            containerFactory = "taskCoordinatorListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(TaskCoordinatorEvent taskCoordinatorEvent) {
        try (CurrentTraceContext.Scope scope = initNewScopeFromExtractedTraceInfo(taskCoordinatorEvent)) {
            Span consumerSpan = tracer.nextSpan()
                    .kind(CONSUMER)
                    .start();

            switch (taskCoordinatorEvent.getPayloadCase()) {
                case ORDERSUBMISSIONEVENT: {
                    OrderSubmissionEvent orderSubmissionEvent = taskCoordinatorEvent.getOrderSubmissionEvent();
                    logger.info("Received OrderSubmissionEvent " + orderSubmissionEvent);

                    consumerSpan
                            .customizer()
                            .name("consume_order_submission_event");

                    orderService.submitOrder(new OrderSubmissionDto(orderSubmissionEvent));

                    consumerSpan.finish();
                    break;
                }
                default: {
                    //TODO Vlad: add metrics
                    logger.info("Received unsupported event type: {}", taskCoordinatorEvent.getPayloadCase());
                }
            }
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
    private CurrentTraceContext.Scope initNewScopeFromExtractedTraceInfo(TaskCoordinatorEvent event) {
        TraceContext.Extractor<Object> extractor = tracing.propagation()
                .extractor((c, key) -> event.getPropertiesMap().get(key));
        return tracing.currentTraceContext().newScope(extractor.extract(event).context());
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }

    @Autowired
    public void setTracing(Tracing tracing) {
        this.tracing = tracing;
    }

    @Autowired
    public void setTracer(Tracer tracer) {
        this.tracer = tracer;
    }
}
