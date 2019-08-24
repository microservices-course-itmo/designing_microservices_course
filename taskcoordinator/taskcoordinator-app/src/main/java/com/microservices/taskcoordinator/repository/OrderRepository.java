package com.microservices.taskcoordinator.repository;

import com.microservices.taskcoordinator.entity.OrderEntity;
import org.springframework.data.repository.CrudRepository;

public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {
}
