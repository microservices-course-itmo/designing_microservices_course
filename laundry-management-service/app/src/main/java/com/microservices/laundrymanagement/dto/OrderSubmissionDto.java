package com.microservices.laundrymanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

@Getter
@Setter
@ToString
public class OrderSubmissionDto {
    private int orderId;
    private List<DetailSubmissionDto> details;
    private int laundryId;
    private int bucketId;
}
