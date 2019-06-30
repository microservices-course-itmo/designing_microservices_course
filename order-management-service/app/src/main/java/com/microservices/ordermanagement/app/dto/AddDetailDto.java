package com.microservices.ordermanagement.app.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
public class AddDetailDto {
    @NotNull
    private Integer pendingDetailId;
    private Integer orderId; // if exists
    @NotNull
    private Integer userId;
}