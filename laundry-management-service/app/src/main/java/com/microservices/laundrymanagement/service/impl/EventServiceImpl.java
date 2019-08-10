package com.microservices.laundrymanagement.service.impl;

import com.microservices.laundrymanagement.entity.EventStatus;
import com.microservices.laundrymanagement.entity.LaundryEventLogEntity;
import com.microservices.laundrymanagement.kafka.producer.EventSender;
import com.microservices.laundrymanagement.repository.LaundryEventRepository;
import com.microservices.laundrymanagement.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final LaundryEventRepository eventRepository;

    private final EventSender eventSender;

    @Autowired
    public EventServiceImpl(LaundryEventRepository eventRepository, EventSender eventSender) {
        this.eventRepository = eventRepository;
        this.eventSender = eventSender;
    }

    @Override
    @Transactional
    public void sendEldestNotSentEvent() {
        Optional<LaundryEventLogEntity> eldestNotSentEvent = eventRepository.findEldestNotSentEvent();
        if (eldestNotSentEvent.isPresent()) {
            LaundryEventLogEntity event = eldestNotSentEvent.get();
            try {
                eventSender.sendMessage(event.getMessage());
            } catch (Throwable t) {
                // TODO sukhoa : deal with exceptions
                logger.info("Failed to send message to Kafka", t);
                return;
            }
            event.setEventStatus(EventStatus.IN_QUEUE);
            eventRepository.save(event);
        }
    }
}
