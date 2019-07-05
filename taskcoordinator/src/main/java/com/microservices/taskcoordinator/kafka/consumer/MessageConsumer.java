package com.microservices.taskcoordinator.kafka.consumer;

import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
public class MessageConsumer {
    @KafkaListener(topics = "${message.topic.name}", groupId = "LaundryManagementServiceEventListener", containerFactory = "laundryManagementListenerContainerFactory")
    public void listen(OrderSubmittedEvent.OrderSubmittedMessage message) {
        System.out.println("Processed message " + message.toString());
    }
}
