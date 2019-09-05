package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderEntity;
import com.microservices.taskcoordinator.entity.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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
