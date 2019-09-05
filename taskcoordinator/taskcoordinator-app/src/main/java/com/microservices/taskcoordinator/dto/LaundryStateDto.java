package com.microservices.taskcoordinator.dto;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import lombok.*;

import java.util.Objects;

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