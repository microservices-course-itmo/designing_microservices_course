package com.microservices.taskcoordinator.dto;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class OrderDetailDto {

    @NotNull
    private Integer id;

    @NotNull
    @Min(value = 0L, message = "Weight must a positive value")
    private Integer weight;

    @NotNull
    @Min(value = 0L, message = "Wash duration must be positive")
    private Long duration;

    @NotNull
    private Integer orderId;
}