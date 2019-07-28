package com.microservices.ordermanagement.app.kafka.producer;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Component;
import org.springframework.util.concurrent.ListenableFuture;

import java.util.concurrent.ExecutionException;

@Component
public class EventSender {

    private final KafkaTemplate<String, byte[]> kafkaTemplate;

    @Value(value = "${order.management.topic.name}")
    private String topicName;

    @Autowired
    public EventSender(KafkaTemplate<String, byte[]> kafkaTemplate) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Retryable
    public void sendMessage(byte[] message) throws Throwable {
        ListenableFuture<SendResult<String, byte[]>> future = kafkaTemplate.send(topicName, message);
        try {
            future.get();
        } catch (ExecutionException e) {
            // TODO Log exception
            throw e.getCause();
        }
    }
}
