package com.microservices.ordermanagement.app.scheduled;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.ordermanagement.app.api.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledEventSender {

    private EventService eventService;

    @Scheduled(fixedDelay = 50)
    @Transactional
    public void sendEvent() throws InvalidProtocolBufferException {
        eventService.sendEldestNotSentEvent();
    }

    @Autowired
    public void setEventService(EventService eventService) {
        this.eventService = eventService;
    }
}
