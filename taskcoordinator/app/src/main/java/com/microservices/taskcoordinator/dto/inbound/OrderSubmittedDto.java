package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper;
import com.microservices.laundrymanagement.api.messages.OrderSubmittedEventWrapper.OrderSubmittedEvent;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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

    public OrderSubmittedDto(OrderSubmittedEvent orderSubmittedEvent) {
        Objects.requireNonNull(orderSubmittedEvent);

        this.orderId = orderSubmittedEvent.getOrderId();
        this.laundryState = new InboundLaundryStateDto(orderSubmittedEvent.getState());
    }
}
