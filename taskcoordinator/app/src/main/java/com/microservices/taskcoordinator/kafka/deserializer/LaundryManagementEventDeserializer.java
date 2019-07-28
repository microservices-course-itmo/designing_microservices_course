package com.microservices.taskcoordinator.kafka.deserializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class LaundryManagementEventDeserializer implements Deserializer<LaundryManagementEvent> {
    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public LaundryManagementEvent deserialize(String topic, byte[] data) {
        try {
            return LaundryManagementEvent.parseFrom(data);
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
