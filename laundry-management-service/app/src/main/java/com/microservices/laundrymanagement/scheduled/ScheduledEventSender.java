package com.microservices.laundrymanagement.scheduled;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.entity.EventStatus;
import com.microservices.laundrymanagement.entity.LaundryEventLogEntity;
import com.microservices.laundrymanagement.repository.LaundryEventRepository;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Component
public class ScheduledEventSender {
    private final Logger logger = LoggerFactory.getLogger(ScheduledEventSender.class);

    private final LaundryEventRepository eventRepository;

    @Autowired
    public ScheduledEventSender(LaundryEventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Scheduled(fixedDelay = 100000)
    @Transactional
    public void sendEvent() throws InvalidProtocolBufferException {
        Optional<LaundryEventLogEntity> eldestNotSentEvent = eventRepository.findEldestNotSentEvent();
        if (eldestNotSentEvent.isPresent()) {
            LaundryEventLogEntity event = eldestNotSentEvent.get();
            OrderSubmittedEventWrapper.OrderSubmittedEvent message = OrderSubmittedEventWrapper.OrderSubmittedEvent.parseFrom(eldestNotSentEvent.get().getMessage());
            logger.info("Sending event {}", message.toString());

            event.setEventStatus(EventStatus.IN_PROCESS);
            eventRepository.save(event);
        }
    }
}
