package com.microservices.ordermanagement.api.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AssignTariffDto {
    @NotNull
    private Integer orderId;

    @NotNull
    private Integer detailId;

    @NotNull
    @Valid
    private TariffDto tariffDto;

    public AssignTariffDto(){}
}

