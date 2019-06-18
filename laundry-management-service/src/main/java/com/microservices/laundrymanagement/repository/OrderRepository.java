package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.model.Order;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Integer> {
    List<Order> findByQueueId(Integer queueId);
}
