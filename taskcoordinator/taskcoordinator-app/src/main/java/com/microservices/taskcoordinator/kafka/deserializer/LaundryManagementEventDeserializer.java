package com.microservices.taskcoordinator.kafka.deserializer;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import org.apache.kafka.common.serialization.Deserializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

public class LaundryManagementEventDeserializer implements Deserializer<LaundryManagementEvent> {
    private final Logger logger = LoggerFactory.getLogger(LaundryManagementEventDeserializer.class);

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public LaundryManagementEvent deserialize(String topic, byte[] data) {
        try {
            return LaundryManagementEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            logger.error("Unable to deserialize LaundryManagementEvent", e);
            return null;
        }
    }

    @Override
    public void close() {
        //nothing to do
    }
}
