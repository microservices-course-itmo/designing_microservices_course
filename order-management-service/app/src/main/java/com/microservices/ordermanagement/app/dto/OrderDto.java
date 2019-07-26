package com.microservices.ordermanagement.app.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.microservices.ordermanagement.app.entity.OrderStatus;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class OrderDto {

    @NotNull
    private Integer id;

    @NotNull
    private OrderStatus status;

    @NotBlank
    private String username;

    @NonNull
    private BigDecimal totalPrice;

    @Valid
    @NotNull
    private List<OrderDetailDto> detailEntities;
}
