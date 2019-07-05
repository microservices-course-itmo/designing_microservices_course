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
public class LaundryStateDTO {

    private Integer laundryId;

    private Long queueWaitingTime;

    private Long reservedTime;

    private Integer version;

    public LaundryStateDTO(LaundryStateEntity laundryStateEntity) {
        this.laundryId = laundryStateEntity.getId();
        this.queueWaitingTime = laundryStateEntity.getQueueWaitingTime();
        this.reservedTime = laundryStateEntity.getReservedTime();
        this.version = laundryStateEntity.getVersion();
    }
}