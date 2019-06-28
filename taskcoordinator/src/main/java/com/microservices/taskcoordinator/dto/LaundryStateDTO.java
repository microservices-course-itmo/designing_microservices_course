package com.microservices.taskcoordinator.dto;

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

    /*public LaundryStateDTO()*/
}
