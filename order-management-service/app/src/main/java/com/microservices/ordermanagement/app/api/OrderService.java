package com.microservices.ordermanagement.app.api;

import com.microservices.ordermanagement.api.dto.AddDetailDto;
import com.microservices.ordermanagement.api.dto.AssignTariffDto;
import com.microservices.ordermanagement.api.dto.OrderDto;
import com.microservices.ordermanagement.api.dto.OrderStatus;
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

    /**
     * Sets passed {@link OrderStatus} to the order associated with given id if
     * this status transition is allowed. For example transition from {@link OrderStatus#PENDING}
     * to {@link OrderStatus#COMPLETE} is not allowed because we can't just skip the {@link OrderStatus#CREATED}
     * stage.
     *
     * @return the representational state of {@link OrderEntity} after operation applying
     */
    OrderDto changeOrderStatus(int orderId, OrderStatus orderStatus);
}
