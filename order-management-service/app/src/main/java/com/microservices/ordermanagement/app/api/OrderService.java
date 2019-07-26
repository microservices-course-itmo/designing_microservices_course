package com.microservices.ordermanagement.app.api;

import com.microservices.ordermanagement.app.dto.AddDetailDto;
import com.microservices.ordermanagement.app.dto.AssignTariffDto;
import com.microservices.ordermanagement.app.dto.OrderDto;
import com.microservices.ordermanagement.app.entity.OrderEntity;

public interface OrderService {

    /**
     * Returns order by given id
     * Throws {@link IllegalArgumentException} if there is no such order
     * in underlying database
     */
    OrderEntity getOrderById(int orderId);

    /**
     * Bind existing pending detail with existing order.
     * in case there is no such detail thrown {@link IllegalArgumentException}
     * in case there is no such order creates new order in underlying database
     *
     * @param addDetailDto contains ids of order and detail
     * @return representative state of {@link OrderEntity} after operation is finished
     */
    OrderDto addDetailToOrder(AddDetailDto addDetailDto);

    /**
     * Assign given tariff to detail associated with given id in order associated with given id
     * If no such order exists throws {@link IllegalArgumentException}
     * If no such detail in order exists throws {@link IllegalArgumentException}
     * @param assignTariffDto contains all sufficient parameters for operation
     * @return representative state of {@link OrderEntity} after operation is finished
     */
    OrderDto assignTariffToOrderDetail(AssignTariffDto assignTariffDto);
}
