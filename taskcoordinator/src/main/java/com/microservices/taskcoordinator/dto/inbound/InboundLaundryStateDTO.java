package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class InboundLaundryStateDTO {

    private int laundryId;
    private long queueWaitingTime;
    private int version;

    public InboundLaundryStateDTO(LaundryStateEntity laundryStateEntity) {
        this.laundryId = laundryStateEntity.getId();
        this.queueWaitingTime = laundryStateEntity.getQueueWaitingTime();
        this.version = laundryStateEntity.getVersion();
    }
}
