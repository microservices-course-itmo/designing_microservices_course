package com.microservices.laundrymanagement.scheduled;

import com.google.protobuf.InvalidProtocolBufferException;
import com.microservices.laundrymanagement.entity.EventStatus;
import com.microservices.laundrymanagement.entity.LaundryEventLogEntity;
import com.microservices.laundrymanagement.kafka.producer.MessageSender;
import com.microservices.laundrymanagement.repository.LaundryEventRepository;
import com.microservices.laundrymanagementapi.messages.OrderSubmittedEvent;
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

    private MessageSender messageSender;

    private final LaundryEventRepository eventRepository;

    @Autowired
    public ScheduledEventSender(LaundryEventRepository eventRepository, MessageSender messageSender) {
        this.eventRepository = eventRepository;
        this.messageSender = messageSender;
    }

    @Scheduled(fixedDelay = 100000)
    @Transactional
    public void sendEvent() throws InvalidProtocolBufferException {
        Optional<LaundryEventLogEntity> eldestNotSentEvent = eventRepository.findEldestNotSentEvent();
        if (eldestNotSentEvent.isPresent()) {
            LaundryEventLogEntity event = eldestNotSentEvent.get();
            OrderSubmittedEvent.OrderSubmittedMessage message = OrderSubmittedEvent.OrderSubmittedMessage.parseFrom(eldestNotSentEvent.get().getMessage());
            logger.info("Sending event {}", message.toString());
            messageSender.sendMessage(message);
            event.setEventStatus(EventStatus.IN_PROCESS);
            eventRepository.save(event);
        }
    }
}
