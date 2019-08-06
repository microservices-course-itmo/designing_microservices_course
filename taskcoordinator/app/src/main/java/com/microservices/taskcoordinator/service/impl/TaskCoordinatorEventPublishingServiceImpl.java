package com.microservices.taskcoordinator.service.impl;

import brave.Span;
import brave.Tracer;
import brave.Tracing;
import com.microservices.taskcoordinator.api.messages.OrderDetailWrapper;
import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.EventType;
import com.microservices.taskcoordinator.entity.TaskCoordinatorEventLogEntity;
import com.microservices.taskcoordinator.repository.TaskCoordinatorEventRepository;
import com.microservices.taskcoordinator.service.TaskCoordinatorEventPublishingService;
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
public class TaskCoordinatorEventPublishingServiceImpl implements TaskCoordinatorEventPublishingService {

    private TaskCoordinatorEventRepository taskCoordinatorEventRepository;

    /**
     * Objects from Brave library for accessing current trace, creating spans and so on
     */
    private Tracer tracer;

    /**
     * Object from Brave library which provides utilities needed for trace instrumentation.
     */
    private Tracing tracing;


    @Autowired
    public TaskCoordinatorEventPublishingServiceImpl(TaskCoordinatorEventRepository taskCoordinatorEventRepository, Tracer tracer, Tracing tracing) {
        this.taskCoordinatorEventRepository = taskCoordinatorEventRepository;
        this.tracer = tracer;
        this.tracing = tracing;
    }

    @Override
    // should be always performed in current transaction if exists to support transactional messaging pattern
    @Transactional(propagation = Propagation.REQUIRED)
    @NewSpan(name = "publish_order_submission_event")
    public void buildAndPublishOrderSubmissionEvent(OrderSubmissionDto orderSubmissionDto) {
        Span oneWaySend = tracer.currentSpan() // might return null
                .kind(PRODUCER)
                .start();

        List<OrderDetailWrapper.OrderDetail> orderDetails = orderSubmissionDto.getDetails().stream()
                .map(od -> OrderDetailWrapper.OrderDetail.newBuilder()
                        .setDetailId(od.getId())
                        .setOrderId(od.getOrderId())
                        .setDuration(od.getDuration())
                        .setWeight(od.getWeight())
                        .build())
                .collect(Collectors.toList());

        OrderSubmissionEventWrapper.OrderSubmissionEvent orderSubmissionEvent = OrderSubmissionEventWrapper
                .OrderSubmissionEvent.newBuilder()
                .setOrderId(orderSubmissionDto.getOrderId())
                .setLaundryId(orderSubmissionDto.getLaundryId())
                .setBucket(orderSubmissionDto.getBucket())
                .addAllDetails(orderDetails)
                .build();

        TaskCoordinatorEventWrapper.TaskCoordinatorEvent taskCoordinatorEvent = TaskCoordinatorEventWrapper.TaskCoordinatorEvent.newBuilder()
                .setOrderSubmissionEvent(orderSubmissionEvent)
                .putAllProperties(createTracingPropertiesToDistribute(oneWaySend))
                .build();

        TaskCoordinatorEventLogEntity taskCoordinatorEventLogEntity = new TaskCoordinatorEventLogEntity(
                EventType.ORDER_SUBMISSION_EVENT, taskCoordinatorEvent.toByteArray());

        taskCoordinatorEventRepository.save(taskCoordinatorEventLogEntity);

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
