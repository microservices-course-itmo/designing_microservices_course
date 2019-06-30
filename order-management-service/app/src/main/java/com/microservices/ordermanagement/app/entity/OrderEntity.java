package com.microservices.ordermanagement.app.entity;

import com.microservices.ordermanagement.app.dto.AssignTariffDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Enumerated(value = EnumType.STRING)
    private OrderStatus status = OrderStatus.PENDING;

    private int userId;

    private long createdTime = System.currentTimeMillis();

    private long estimatedTime;

    @NonNull
    private BigDecimal totalPrice = BigDecimal.ZERO;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "orderId")
    private List<OrderDetailEntity> detailEntities = new ArrayList<>();

    public void addPendingDetail(PendingDetailEntity pendingDetail) {
        if (detailEntities.stream().anyMatch(d -> d.getId() == pendingDetail.getId())) {
            return;
        }

        detailEntities.add(new OrderDetailEntity(pendingDetail.getId(), this.getId(), pendingDetail.getWeight()));
    }

    public void assignTariffToOrderDetail(AssignTariffDto assignTariffDto) {
        OrderDetailEntity orderDetail = detailEntities.stream()
                .filter(d -> d.getId() == assignTariffDto.getDetailId())
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("There is no detail with id: " +
                        assignTariffDto.getDetailId() + "" + "in order id: " + assignTariffDto.getOrderId() + " in database"));

        orderDetail.addTariffInformation(assignTariffDto.getTariffDto());
        this.setTotalPrice(detailEntities.stream()
                .map(OrderDetailEntity::getPrice)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add));
    }
}
