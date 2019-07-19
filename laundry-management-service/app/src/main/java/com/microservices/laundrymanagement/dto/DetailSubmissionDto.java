package com.microservices.laundrymanagement.dto;

import com.microservices.taskcoordinator.api.messages.OrderDetailWrapper;
import com.microservices.taskcoordinator.api.messages.OrderDetailWrapper.OrderDetail;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetailSubmissionDto {
    private int detailId;
    private int weight;
    private long duration;

    public DetailSubmissionDto(OrderDetail orderDetailMessage) {
        this.detailId = orderDetailMessage.getDetailId();
        this.weight = orderDetailMessage.getWeight();
        this.duration = orderDetailMessage.getDuration();
    }
}
