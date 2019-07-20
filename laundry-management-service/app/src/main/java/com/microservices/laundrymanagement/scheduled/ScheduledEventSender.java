package com.microservices.laundrymanagement.scheduled;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class ScheduledEventSender {
    private EventService eventService;

    @Autowired
    public ScheduledEventSender(EventService eventService) {
        this.eventService = eventService;
    }

    @Scheduled(fixedDelay = 2) // TODO sukhoa make it configurable
    public void sendEvent() throws InvalidProtocolBufferException {
        eventService.sendEldestNotSentEvent();
    }
}
