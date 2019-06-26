package com.microservices.taskcoordinator.dto;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
public class OrderDetailDTO {

    private int detailId;
    private int weight;
    private long duration;
}
