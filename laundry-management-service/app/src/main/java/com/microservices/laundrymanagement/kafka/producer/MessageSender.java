package com.microservices.laundrymanagement.kafka.producer;

import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

@Component
public class MessageSender {

    private final KafkaTemplate<String, OrderSubmittedEvent.OrderSubmittedMessage> kafkaTemplate;

    @Value(value = "${laundry.management.topic.name}")
    private String topicName;

    @Autowired
    public MessageSender(KafkaTemplate<String, OrderSubmittedEvent.OrderSubmittedMessage> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }


    public void sendMessage(OrderSubmittedEvent.OrderSubmittedMessage message) {

        ListenableFuture<SendResult<String, OrderSubmittedEvent.OrderSubmittedMessage>> future = kafkaTemplate.send(topicName, message);

        future.addCallback(new ListenableFutureCallback<SendResult<String, OrderSubmittedEvent.OrderSubmittedMessage>>() {

            @Override
            public void onSuccess(SendResult<String, OrderSubmittedEvent.OrderSubmittedMessage> result) {
                System.out.println("Sent message=[" + message + "] with offset=[" + result.getRecordMetadata().offset() + "]");
            }

            @Override
            public void onFailure(Throwable ex) {
                System.out.println("Unable to send message=[" + message + "] due to : " + ex.getMessage());
            }
        });
    }
}
