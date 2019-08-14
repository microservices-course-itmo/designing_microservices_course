package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.ordermanagement.api.events.OrderCreatedEventWrapper.OrderCreatedEvent;
import com.microservices.taskcoordinator.dto.OrderDetailDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderCoordinationDto {

    @NotNull
    private Integer orderId;

    @NotNull
    private List<OrderDetailDto> details;

    public OrderCoordinationDto(OrderCreatedEvent orderCreatedEvent) {
        this.orderId = orderCreatedEvent.getOrder().getOrderId();
        this.details = orderCreatedEvent.getOrder().getDetailsList().stream()
                .map(d -> new OrderDetailDto(
                        d.getDetailId(),
                        d.getWeight(),
                        d.getDuration(),
                        d.getOrderId()))
                .collect(Collectors.toList());
    }
}
