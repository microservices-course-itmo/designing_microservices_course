package com.microservices.laundrymanagement.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DetailSubmissionDto {
    private int detailId;
    private int weight;
    private long time;
}
