package com.microservices.laundrymanagement.kafka.consumer;

import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.service.OrderService;
import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper.TaskCoordinatorEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class TaskCoordinatorEventConsumer {

    private final Logger logger = LoggerFactory.getLogger(TaskCoordinatorEventConsumer.class);


    private OrderService orderService;

    @KafkaListener(
            topics = "${taskcoordinator.topic.name}",
            groupId = "${taskcoordinator.listener.name}",
            containerFactory = "taskCoordinatorListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(TaskCoordinatorEvent taskCoordinatorEvent) {

        switch (taskCoordinatorEvent.getPayloadCase()) {
            case ORDERSUBMISSIONEVENT: {
                OrderSubmissionEvent orderSubmissionEvent = taskCoordinatorEvent.getOrderSubmissionEvent();
                System.out.println("Received OrderSubmissionEvent " + orderSubmissionEvent);

                orderService.submitOrder(new OrderSubmissionDto(orderSubmissionEvent));
                break;
            }
            default: {
                //TODO Vlad: add metrics
                logger.info("Received unsupported event type: {}", taskCoordinatorEvent.getPayloadCase());
            }
        }
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
