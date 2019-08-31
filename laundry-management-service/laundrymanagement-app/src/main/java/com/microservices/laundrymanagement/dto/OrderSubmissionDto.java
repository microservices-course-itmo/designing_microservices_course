package com.microservices.laundrymanagement.dto;

import com.microservices.taskcoordinator.api.messages.OrderSubmissionEventWrapper.OrderSubmissionEvent;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class OrderSubmissionDto {
    private Integer orderId;
    private List<DetailSubmissionDto> details;
    private Integer laundryId;
    private Integer bucket;

    public OrderSubmissionDto(OrderSubmissionEvent orderSubmissionEvent) {
        Objects.requireNonNull(orderSubmissionEvent);

        this.orderId = orderSubmissionEvent.getOrderId();
        this.laundryId = orderSubmissionEvent.getLaundryId();
        this.bucket = orderSubmissionEvent.getBucket();
        this.details = orderSubmissionEvent.getDetailsList() == null
                ? null
                : orderSubmissionEvent.getDetailsList().stream()
                .map(DetailSubmissionDto::new)
                .collect(Collectors.toList());
    }
}
