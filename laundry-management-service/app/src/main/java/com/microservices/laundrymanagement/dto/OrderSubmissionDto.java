package com.microservices.laundrymanagement.dto;

import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
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

    public OrderSubmissionDto(OrderSubmissionEvent orderSubmissionEvent) {

    }
}
