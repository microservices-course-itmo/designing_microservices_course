package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
public class LaundryStateDto {

    private Integer id;

    private Long queueWaitingTime;

    private Long reservedTime;

    private Integer version;

    public LaundryStateDto(LaundryStateEntity laundryStateEntity) {
        Objects.requireNonNull(laundryStateEntity);

        this.id = laundryStateEntity.getId();
        this.queueWaitingTime = laundryStateEntity.getQueueWaitingTime();
        this.reservedTime = laundryStateEntity.getReservedTime();
        this.version = laundryStateEntity.getVersion();
    }


}