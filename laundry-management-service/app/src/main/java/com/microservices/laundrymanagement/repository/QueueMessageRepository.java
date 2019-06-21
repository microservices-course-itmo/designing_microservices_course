package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.OrderSubmittedMessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface QueueMessageRepository extends CrudRepository<OrderSubmittedMessageEntity, Integer> {
}
