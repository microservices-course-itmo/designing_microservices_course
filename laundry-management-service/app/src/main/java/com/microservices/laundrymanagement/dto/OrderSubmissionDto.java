package com.microservices.laundrymanagement.dto;

import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
public class OrderSubmissionDto {
    private int orderId;
    private List<DetailSubmissionDto> details;
    private int laundryId;
    private int bucket;

    public OrderSubmissionDto(OrderSubmissionEvent orderSubmissionEvent) {
        this.orderId = orderSubmissionEvent.getOrderId();
        this.laundryId = orderSubmissionEvent.getLaundryId();
        this.bucket = orderSubmissionEvent.getBucket();
        this.details = orderSubmissionEvent.getDetailsList().stream()
                .map(DetailSubmissionDto::new)
                .collect(Collectors.toList());
    }
}
