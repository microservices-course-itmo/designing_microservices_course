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

    int laundryId;
    long queueWaitingTime;
    int version;
}
