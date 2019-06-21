package com.microservices.laundrymanagement.entity;

import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
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

    private long estimatedTime;

    @Enumerated(value = EnumType.STRING)

    private OrderStatus status;

    private int laundryId;

    private int bucket;

    public OrderEntity(OrderSubmissionDto orderSubmissionDto) {
        this.id = orderSubmissionDto.getOrderId();
        this.estimatedTime = orderSubmissionDto.getDetails().stream()
                .map(DetailSubmissionDto::getTime)
                .reduce(0L, Long::sum);
        this.status = OrderStatus.QUEUED;
        this.laundryId = orderSubmissionDto.getLaundryId();
        this.bucket = orderSubmissionDto.getBucketId();
    }

}
