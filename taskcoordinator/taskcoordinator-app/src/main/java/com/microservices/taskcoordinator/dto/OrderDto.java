package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderStatus;
import lombok.*;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@ToString
public class OrderDto {

    private Integer id;

    private Integer laundryId;

    private Integer bucket;

    private Long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private Long estimatedTime;

    private Long completionTime;

    private List<OrderDetailDto> details;
}
