package com.microservices.laundrymanagement.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class DetailSubmissionDto {
    private int detailId;
    private int weight;
    private long time;
}
