package com.microservices.taskcoordinator.kafka.consumer;

import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import com.microservices.laundrymanagement.api.messages.OrderProcessedEventWrapper.OrderProcessedEvent;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper.OrderSubmittedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @KafkaListener(topics = "${laundry.management.topic.name}",
            groupId = "LaundryManagementServiceEventListener",
            containerFactory = "laundryManagementListenerContainerFactory",
            autoStartup = "${kafka.activateConsumers}")
    public void listen(LaundryManagementEvent message) {

        switch (message.getPayloadCase()) {
            case ORDERPROCESSEDEVENT: {
                OrderProcessedEvent event = message.getOrderProcessedEvent();
                System.out.println("Received OrderProcessedEvent " + event);
                break;
            }
            case ORDERSUBMITTEDEVENT: {
                OrderSubmittedEvent event = message.getOrderSubmittedEvent();
                System.out.println("Received OrderSubmittedEvent " + event);
                break;
            }
            default: {
                // TODO Vlad : log and throw exception
                throw new IllegalArgumentException("Incorrect event received");
            }
        }
    }
}
