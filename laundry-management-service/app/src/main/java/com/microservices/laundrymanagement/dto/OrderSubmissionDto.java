package com.microservices.laundrymanagement.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class OrderSubmissionDto {
    private int orderId;
    private List<DetailSubmissionDto> details;
    private int laundryId;
    private int bucketId;
}
