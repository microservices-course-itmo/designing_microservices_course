package com.microservices.laundrymanagement.kafka.message;

import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
import org.apache.kafka.common.serialization.Serializer;

import java.util.Map;

public class OrderSubmittedMessageSerializer implements Serializer<OrderSubmittedEvent.OrderSubmittedMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public byte[] serialize(String topic, OrderSubmittedEvent.OrderSubmittedMessage data) {
        return data.toByteArray();
    }

    @Override
    public void close() {
        //nothing to do
    }
}
