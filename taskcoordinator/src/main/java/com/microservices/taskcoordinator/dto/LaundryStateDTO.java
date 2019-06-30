package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class LaundryStateDTO {

    private int laundryId;
    private long queueWaitingTime;
    private int version;

    public LaundryStateDTO(LaundryStateEntity laundryStateEntity) {
        this.laundryId = laundryStateEntity.getId();
        this.queueWaitingTime = laundryStateEntity.getQueueWaitingTime();
        this.version = laundryStateEntity.getVersion();
    }
}
