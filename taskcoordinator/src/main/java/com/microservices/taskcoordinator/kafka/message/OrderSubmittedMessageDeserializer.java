package com.microservices.taskcoordinator.kafka.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class OrderSubmittedMessageDeserializer implements Deserializer<OrderSubmittedEvent.OrderSubmittedMessage> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public OrderSubmittedEvent.OrderSubmittedMessage deserialize(String topic, byte[] data) {
        try {
            return OrderSubmittedEvent.OrderSubmittedMessage.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            return null;
        }
    }

    @Override
    public void close() {
        //nothing to do
    }
}
