package com.microservices.taskcoordinator.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class InboundLaundryStateDto {

    @NotNull
    private Integer laundryId;

    @NotNull
    @Min(value = 0L, message = "Queue waiting time must be positive")
    private Long queueWaitingTime;

    @NotNull
    private Integer version;

}
