package com.microservices.taskcoordinator.dto.inbound;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class OrderCoordinationDTO {

    private Integer orderId;

    private List<OrderDetailDTO> details;
}
