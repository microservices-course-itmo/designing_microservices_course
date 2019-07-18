package com.microservices.taskcoordinator.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class OrderProcessedDto {

    @NotNull
    Integer orderId;

    @Valid
    @NotNull
    InboundLaundryStateDto laundryState;

    @Min(value = 0L, message = "Order completion time must be positive")
    @NotNull
    Long completionTime;
}
