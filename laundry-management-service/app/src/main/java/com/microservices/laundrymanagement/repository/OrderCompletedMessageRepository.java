package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.OrderCompletedMessageEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderCompletedMessageRepository extends CrudRepository<OrderCompletedMessageEntity, Integer> {
}
