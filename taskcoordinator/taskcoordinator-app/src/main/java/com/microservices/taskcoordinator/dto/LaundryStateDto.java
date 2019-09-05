package com.microservices.taskcoordinator.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class LaundryStateDto {

    private Integer id;

    private Long queueWaitingTime;

    private Long reservedTime;

    private Integer version;
}