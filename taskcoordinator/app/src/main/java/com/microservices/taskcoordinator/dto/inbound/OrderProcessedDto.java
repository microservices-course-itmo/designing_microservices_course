package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.laundrymanagement.api.messages.OrderProcessedEventWrapper.OrderProcessedEvent;
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
    private Integer orderId;

    @Valid
    @NotNull
    private InboundLaundryStateDto laundryState;

    @Min(value = 0L, message = "Order completion time must be positive")
    @NotNull
    private Long completionTime;

    public OrderProcessedDto(OrderProcessedEvent orderProcessedEvent) {
        this.orderId = orderProcessedEvent.getOrderId();
        this.laundryState = new InboundLaundryStateDto(orderProcessedEvent.getState());
        this.completionTime = orderProcessedEvent.getCompleteTime();
    }
}
