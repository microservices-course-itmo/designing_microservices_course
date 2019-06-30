package com.microservices.ordermanagement.app.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;

@Getter
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AssignTariffDto {
    Integer orderId;
    Integer detailId;
    TariffDto tariffDto;
}
