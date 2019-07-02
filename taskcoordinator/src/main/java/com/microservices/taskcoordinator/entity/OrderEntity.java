package com.microservices.taskcoordinator.entity;

import com.microservices.taskcoordinator.dto.OrderDetailDTO;
import com.microservices.taskcoordinator.dto.inbound.OrderCoordinationDTO;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.CascadeType;
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
    private Integer id;

    private Integer laundryId;

    private Integer bucket;

    private Long duration;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    private Long estimatedTime;

    private Long completionTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
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
        this.details = orderCoordinationDTO.getDetails() == null
                ? null
                : orderCoordinationDTO.getDetails().stream()
                        .map(OrderDetailEntity::new)
                        .collect(Collectors.toList());
    }


}
