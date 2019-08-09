package com.microservices.taskcoordinator.dto.outbound;

import com.microservices.taskcoordinator.dto.OrderDetailDto;
import com.microservices.taskcoordinator.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderSubmissionDto {

    private Integer orderId;

    private Integer laundryId;

    private Integer bucket;

    private List<OrderDetailDto> details;

    public OrderSubmissionDto(OrderEntity orderEntity) {
        Objects.requireNonNull(orderEntity);

        this.orderId = orderEntity.getId();
        this.laundryId = orderEntity.getLaundryId();
        this.bucket = orderEntity.getBucket();
        this.details = orderEntity.getDetails() == null
                ? null
                : orderEntity.getDetails().stream()
                    .map(OrderDetailDto::new)
                    .collect(Collectors.toList());
    }
}
