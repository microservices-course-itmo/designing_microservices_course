package com.microservices.taskcoordinator.dto.inbound;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@EqualsAndHashCode
@NoArgsConstructor
public class OrderSubmittedDto {

    @NotNull
    private Integer orderId;

    @Valid
    @NotNull
    private InboundLaundryStateDto laundryState;
}
