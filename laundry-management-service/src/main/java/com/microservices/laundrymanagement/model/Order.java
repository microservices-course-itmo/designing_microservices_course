package com.microservices.laundrymanagement.model;

import com.microservices.laundrymanagement.dto.OrderRequest;

public class Order {
    private int orderId;
    private long estimatedTime;
    private double price;
    private OrderStatus status;
    private int laundryId;
    private int bucketId;

    public Order(OrderRequest orderRequest, OrderStatus orderStatus) {
        this.orderId = orderRequest.getOrderId();
        this.estimatedTime = orderRequest.getEstimatedTime();
        this.price = orderRequest.getPrice();
        this.laundryId = orderRequest.getLaundryId();
        this.bucketId = orderRequest.getBucketId();
        this.status = orderStatus;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public int getOrderId() {
        return orderId;
    }
}
