package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

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
        this.id = laundryStateEntity.getId();
        this.queueWaitingTime = laundryStateEntity.getQueueWaitingTime();
        this.reservedTime = laundryStateEntity.getReservedTime();
        this.version = laundryStateEntity.getVersion();
    }

    public long getCompletionTimePrediction() {
        return this.getQueueWaitingTime() + this.getReservedTime();
    }
}