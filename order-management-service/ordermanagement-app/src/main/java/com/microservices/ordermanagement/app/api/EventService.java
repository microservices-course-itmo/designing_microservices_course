package com.microservices.ordermanagement.app.api;

import com.google.protobuf.InvalidProtocolBufferException;

public interface EventService {
    void sendEldestNotSentEvent() throws InvalidProtocolBufferException;
}
