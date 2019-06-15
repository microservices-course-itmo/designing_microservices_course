package com.microservices.laundrymanagement.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private int orderId;
    private long estimatedTime;
    private double price;

    private int laundryId;
    private int bucketId;
}
