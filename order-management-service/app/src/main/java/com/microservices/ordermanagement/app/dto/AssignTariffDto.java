package com.microservices.ordermanagement.app.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AssignTariffDto {
    @NotNull
    Integer orderId;
    @NotNull
    Integer detailId;
    @NotNull
    TariffDto tariffDto;
}

