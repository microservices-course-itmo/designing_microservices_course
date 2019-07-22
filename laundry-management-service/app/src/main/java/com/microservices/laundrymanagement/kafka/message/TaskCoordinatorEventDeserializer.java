package com.microservices.laundrymanagement.kafka.message;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.api.messages.LaundryManagementEventWrapper.LaundryManagementEvent;
import com.microservices.taskcoordinator.api.messages.TaskCoordinatorEventWrapper;
import org.apache.kafka.common.serialization.Deserializer;

import java.util.Map;

public class TaskCoordinatorEventDeserializer implements Deserializer<TaskCoordinatorEventWrapper.TaskCoordinatorEvent> {

    @Override
    public void configure(Map<String, ?> configs, boolean isKey) {
        //nothing to do
    }

    @Override
    public TaskCoordinatorEventWrapper.TaskCoordinatorEvent deserialize(String topic, byte[] data) {
        try {
            return TaskCoordinatorEventWrapper.TaskCoordinatorEvent.parseFrom(data);
        } catch (InvalidProtocolBufferException e) {
            // TODO afanay : log exception
            return null;
        }
    }

    @Override
    public void close() {
        //nothing to do
    }
}
