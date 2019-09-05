package com.microservices.taskcoordinator.dto.outbound;

import com.microservices.taskcoordinator.dto.OrderDetailDto;
import com.microservices.taskcoordinator.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderSubmissionDto {

    private Integer orderId;

    private Integer laundryId;

    private Integer bucket;

    private List<OrderDetailDto> details;
}
