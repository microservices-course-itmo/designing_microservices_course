package com.microservices.taskcoordinator.kafka.deserializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.ordermanagement.api.events.OrderManagementEventWrapper.OrderManagementEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class OrderManagementEventDeserializer implements Deserializer<OrderManagementEvent> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public OrderManagementEvent deserialize(String topic, byte[] data) {
        try {
            return OrderManagementEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            // TODO Vlad : log exception
            return null;
        }
    }

    @Override
    public void close() {
        //nothing to do
    }
}
