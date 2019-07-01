package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    private int id;

    private int laundryId;

    private int bucket;

    private long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private long estimatedTime;

    private long completionTime;

    @OneToMany
    @JoinColumn(name = "id", referencedColumnName = "id")
    private List<OrderDetailEntity> details;

    public OrderEntity(OrderCoordinationDTO orderCoordinationDTO, int selectedLaundryId, long estimatedTimeToComplete) {
        this.id = orderCoordinationDTO.getOrderId();
        this.laundryId = selectedLaundryId;
        this.bucket = 1;
        this.duration = orderCoordinationDTO.getDetails().stream()
                .map(OrderDetailDTO::getDuration)
                .reduce(0L, Long::sum);
        this.status = OrderStatus.APPROVED;
        this.estimatedTime = estimatedTimeToComplete;
        this.details = orderCoordinationDTO.getDetails()
                .stream()
                .map(OrderDetailEntity::new)
                .collect(Collectors.toList());
    }


}
