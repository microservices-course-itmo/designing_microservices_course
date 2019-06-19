package com.microservices.laundrymanagement.service;

import com.microservices.laundrymanagement.model.Laundry;
import com.microservices.laundrymanagement.model.Order;

import java.util.List;
import java.util.Optional;

public interface LaundryService {
    /**
     * Возвращает следующий исполняемый заказ со статусом {@link com.microservices.laundrymanagement.model.OrderStatus#QUEUED}
     * из минимальной корзины заданной очереди.
     *
     * @param queueId id очереди
     */
    Optional<Order> selectNextOrder(int queueId);

    List<Laundry> getAllLaundries();

    Optional<Laundry> getById(int id);
}
