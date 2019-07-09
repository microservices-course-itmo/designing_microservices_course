package com.microservices.laundrymanagement.scheduled;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.repository.LaundryEventRepository;
import com.microservices.laundrymanagement.service.EventService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ScheduledEventSender {
    private EventService eventService;

    private final LaundryEventRepository eventRepository;

    @Autowired
    public ScheduledEventSender(LaundryEventRepository eventRepository, EventService eventService) {
        this.eventRepository = eventRepository;
        this.eventService = eventService;
    }

    @Scheduled(fixedDelay = 10000)
    @Transactional
    public void sendEvent() throws InvalidProtocolBufferException {
        eventService.sendEldestNotSentEvent();
    }
}
