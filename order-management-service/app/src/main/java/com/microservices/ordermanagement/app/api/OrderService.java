package com.microservices.ordermanagement.app.api;

import com.microservices.ordermanagement.app.dto.AddDetailDto;
import com.microservices.ordermanagement.app.entity.OrderEntity;

public interface OrderService {

    /**
     * Bind existing pending detail with existing order.
     * in case there is no such detail thrown {@link IllegalArgumentException}
     * in case there is no such order creates new order in underlying database
     *
     * @param addDetailDto contains ids of order and detail
     * @return representative state of {@link OrderEntity} after operation is finished
     */
    OrderEntity addDetailToOrder(AddDetailDto addDetailDto);
}
