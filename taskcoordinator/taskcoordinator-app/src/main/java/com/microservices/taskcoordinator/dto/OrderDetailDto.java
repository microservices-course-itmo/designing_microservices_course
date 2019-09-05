package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.OrderDetailEntity;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Objects;

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