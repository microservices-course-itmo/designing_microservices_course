package com.microservices.ordermanagement.app.impl;

import com.microservices.ordermanagement.app.api.EventService;
import com.microservices.ordermanagement.app.entity.EventStatus;
import com.microservices.ordermanagement.app.entity.OrderManagementEventLogEntity;
import com.microservices.ordermanagement.app.kafka.producer.EventSender;
import com.microservices.ordermanagement.app.repository.OrderManagementEventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private static final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private OrderManagementEventRepository orderManagementEventRepository;

    private EventSender eventSender;

    @Autowired
    public EventServiceImpl(OrderManagementEventRepository orderManagementEventRepository, EventSender eventSender) {
        this.orderManagementEventRepository = orderManagementEventRepository;
        this.eventSender = eventSender;
    }

    @Override
    @Transactional
    public void sendEldestNotSentEvent() {
        Optional<OrderManagementEventLogEntity> eldestNotSentEvent = orderManagementEventRepository.findEldestNotSentEvent();
        if (eldestNotSentEvent.isPresent()) {
            OrderManagementEventLogEntity event = eldestNotSentEvent.get();
            try {
                eventSender.sendMessage(event.getMessage());
            } catch (Throwable t) {
                // TODO afanay : deal with exceptions
                logger.info("Failed to send deserializer to Kafka", t);
                return;
            }
            event.setEventStatus(EventStatus.IN_QUEUE);
            orderManagementEventRepository.save(event);
            // TODO sukhoa: log success
        }
    }
}
