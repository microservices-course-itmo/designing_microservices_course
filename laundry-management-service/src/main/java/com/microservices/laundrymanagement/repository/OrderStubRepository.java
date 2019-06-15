package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.model.Order;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class OrderStubRepository implements OrderRepository {
    private Map<Integer, Order> storage = new HashMap<>();

    @Override
    public Iterable<Order> findAll() {
        return storage.values();
    }

    @Override
    public Optional<Order> findById(Integer orderId) {
        return Optional.ofNullable(storage.get(orderId));
    }

    @Override
    public <S extends Order> S save(S order) {
        storage.put(order.getOrderId(), order);
        return order;
    }
}
