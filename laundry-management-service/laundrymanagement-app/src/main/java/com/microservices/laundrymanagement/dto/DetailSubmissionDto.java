package com.microservices.laundrymanagement.dto;

import com.microservices.taskcoordinator.api.messages.OrderDetailWrapper.OrderDetail;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.Objects;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class DetailSubmissionDto {
    private Integer detailId;
    private Integer weight;
    private Long duration;

    public DetailSubmissionDto(OrderDetail orderDetailMessage) {
        Objects.requireNonNull(orderDetailMessage);

        this.detailId = orderDetailMessage.getDetailId();
        this.weight = orderDetailMessage.getWeight();
        this.duration = orderDetailMessage.getDuration();
    }
}
