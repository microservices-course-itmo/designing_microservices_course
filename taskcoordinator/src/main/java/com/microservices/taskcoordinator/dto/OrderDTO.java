package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderStatus;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class OrderDTO {

    private int id;

    private int laundryId;

    private int bucket;

    private long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private long estimatedTime;

    private long completionTime;
}
