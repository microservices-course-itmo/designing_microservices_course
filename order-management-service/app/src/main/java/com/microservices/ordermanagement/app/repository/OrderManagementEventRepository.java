package com.microservices.ordermanagement.app.repository;


import com.microservices.ordermanagement.app.entity.OrderManagementEventLogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderManagementEventRepository extends CrudRepository<OrderManagementEventLogEntity, Integer> {
    @Query(value = "select * from order_management.ordermanagement_events where event_status = 'PENDING' " +
            "order by created_time asc limit 1", nativeQuery = true)
    Optional<OrderManagementEventLogEntity> findEldestNotSentEvent();
}
