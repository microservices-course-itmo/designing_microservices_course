package com.microservices.ordermanagement.app.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AssignTariffDto {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer detailId;

    @NotNull
    @Valid
    private TariffDto tariffDto;
}

