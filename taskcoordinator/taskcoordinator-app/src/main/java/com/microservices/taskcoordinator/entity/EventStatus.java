package com.microservices.taskcoordinator.entity;

public enum EventStatus {
    PENDING,
    IN_QUEUE,
    IN_PROCESS,
    ACKNOWLEDGED,
}
