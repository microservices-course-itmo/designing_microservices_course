package com.microservices.taskcoordinator.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class InboundLaundryStateDto {

    private Integer laundryId;

    private Long queueWaitingTime;

    private Integer version;

}
