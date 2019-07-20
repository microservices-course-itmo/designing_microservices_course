package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.dto.OrderDetailDto;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderCoordinationDto {

    @NotNull
    private Integer orderId;

    @NotNull
    private List<OrderDetailDto> details;
}
