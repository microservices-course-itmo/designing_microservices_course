package com.microservices.laundrymanagement.service;

import com.google.protobuf.InvalidProtocolBufferException;

public interface EventService {
    void sendEldestNotSentEvent() throws InvalidProtocolBufferException;
}
