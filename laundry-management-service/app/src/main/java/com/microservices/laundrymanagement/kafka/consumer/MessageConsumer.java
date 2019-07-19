package com.microservices.laundrymanagement.kafka.consumer;

import com.microservices.laundrymanagement.service.OrderService;
import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper.TaskCoordinatorEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {

    private OrderService orderService;

    @KafkaListener(topics = "${taskcoordinator.topic.name}",
            groupId = "TaskCoordinatorServiceEventListener",
            containerFactory = "taskCoordinatorListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(TaskCoordinatorEvent message) {

        switch (message.getPayloadCase()) {
            case ORDERSUBMISSIONEVENT: {
                OrderSubmissionEvent event = message.getOrderSubmissionEvent();
                System.out.println("Received OrderSubmissionEvent " + event);

                orderService.submitOrder();
                break;
            }
            default: {
                // TODO Afanay : log and throw exception
                throw new IllegalArgumentException("Incorrect event received");
            }
        }
    }

    @Autowired
    public void setOrderService(OrderService orderService) {
        this.orderService = orderService;
    }
}
