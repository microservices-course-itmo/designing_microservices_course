package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.QueueMessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface QueueMessageRepository extends CrudRepository<QueueMessageEntity, Integer> {
}
