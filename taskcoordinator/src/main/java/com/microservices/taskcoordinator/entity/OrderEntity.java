package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.DetailSubmissionDto;
import com.microservices.taskcoordinator.dto.OrderSubmissionDto;
import com.microservices.taskcoordinator.entity.OrderStatus;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private int id;

    private int laundryId;

    //TODO
    private int bucket;

    private long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private long estimatedTime;

    private long actualTime;


    public OrderEntity(OrderSubmissionDto orderSubmissionDto, int selectedLaundryId, long estimatedTimeToComplete) {
        this.id = orderSubmissionDto.getOrderId();
        this.laundryId = selectedLaundryId;
        this.duration = orderSubmissionDto.getDetails().stream()
                .map(DetailSubmissionDto::getTime)
                .reduce(0L, Long::sum);
        this.status = OrderStatus.APPROVED;
        this.estimatedTime = estimatedTimeToComplete;
    }

}
