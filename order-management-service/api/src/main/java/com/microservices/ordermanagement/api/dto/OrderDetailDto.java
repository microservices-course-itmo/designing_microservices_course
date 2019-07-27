package com.microservices.ordermanagement.api.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
class OrderDetailDto {
    @NotNull
    private Integer id;

    @NotNull
    private Integer orderId;

    @NotNull
    private Integer weight;

    private Integer tariffId;

    @NotNull
    @Digits(integer = 4, fraction = 2)
    private BigDecimal price;
}
