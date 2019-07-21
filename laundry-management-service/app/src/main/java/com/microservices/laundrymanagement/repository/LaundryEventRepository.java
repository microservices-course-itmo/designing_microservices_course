package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.LaundryEventLogEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LaundryEventRepository extends CrudRepository<LaundryEventLogEntity, Integer> {
    @Query(value = "select * from laundry_management.laundry_events where event_status = 'PENDING' " +
            "order by created_time asc limit 1", nativeQuery = true)
    Optional<LaundryEventLogEntity> findEldestNotSentEvent();
}
