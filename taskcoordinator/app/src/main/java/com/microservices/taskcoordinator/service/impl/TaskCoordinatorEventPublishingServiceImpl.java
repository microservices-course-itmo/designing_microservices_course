package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.api.messages.OrderDetailWrapper;
import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper;
import com.microservices.taskcoordinator.dto.outbound.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.EventType;
import com.microservices.taskcoordinator.entity.TaskCoordinatorEventLogEntity;
import com.microservices.taskcoordinator.repository.TaskCoordinatorEventRepository;
import com.microservices.taskcoordinator.service.TaskCoordinatorEventPublishingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskCoordinatorEventPublishingServiceImpl implements TaskCoordinatorEventPublishingService {

    private TaskCoordinatorEventRepository taskCoordinatorEventRepository;

    @Autowired
    public TaskCoordinatorEventPublishingServiceImpl(TaskCoordinatorEventRepository taskCoordinatorEventRepository) {
        this.taskCoordinatorEventRepository = taskCoordinatorEventRepository;
    }

    @Override
    // should be always performed in current transaction if exists to support transactional messaging pattern
    @Transactional(propagation = Propagation.REQUIRED)
    public void buildAndPublishOrderSubmissionEvent(OrderSubmissionDto orderSubmissionDto) {

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
                .build();

        TaskCoordinatorEventLogEntity taskCoordinatorEventLogEntity = new TaskCoordinatorEventLogEntity(
                EventType.ORDER_SUBMISSION_EVENT, taskCoordinatorEvent.toByteArray());

        taskCoordinatorEventRepository.save(taskCoordinatorEventLogEntity);
    }

}
