package com.microservices.taskcoordinator.service.impl;

import com.microservices.taskcoordinator.entity.EventStatus;
import com.microservices.taskcoordinator.entity.TaskCoordinatorEventLogEntity;
import com.microservices.taskcoordinator.kafka.producer.EventSender;
import com.microservices.taskcoordinator.repository.TaskCoordinatorEventRepository;
import com.microservices.taskcoordinator.service.EventService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class EventServiceImpl implements EventService {

    private final TaskCoordinatorEventRepository taskCoordinatorEventRepository;

    private final Logger logger = LoggerFactory.getLogger(EventServiceImpl.class);

    private final EventSender eventSender;

    @Autowired
    public EventServiceImpl(TaskCoordinatorEventRepository taskCoordinatorEventRepository, EventSender eventSender) {
        this.taskCoordinatorEventRepository = taskCoordinatorEventRepository;
        this.eventSender = eventSender;
    }

    @Override
    @Transactional
    public void sendEldestNotSentEvent() {
        Optional<TaskCoordinatorEventLogEntity> eldestNotSentEvent = taskCoordinatorEventRepository.findEldestNotSentEvent();

        if (eldestNotSentEvent.isPresent()) {
            TaskCoordinatorEventLogEntity event = eldestNotSentEvent.get();
            logger.info("Trying to send eldest not sent event with id = {}", event.getId());

            try {
                eventSender.sendMessage(event.getMessage());
            } catch (Throwable t) {
                // TODO afanay : deal with exceptions
                logger.info("Failed to send message to Kafka", t);
                return;
            }

            event.setEventStatus(EventStatus.IN_QUEUE);
            taskCoordinatorEventRepository.save(event);

            logger.info("Event with id = {} has been sent and has status = {}", event.getId(), event.getEventStatus());
        }
    }
}
