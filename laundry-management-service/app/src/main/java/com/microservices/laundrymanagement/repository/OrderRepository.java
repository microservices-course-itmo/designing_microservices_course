package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.OrderEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends CrudRepository<OrderEntity, Integer> {

    /**
     * For testing purposes only.
     * Helps to emulate laundry workers activity. Takes some order from the
     * oldest (having smallest number) bucket they had
     */
    @Query(value = "select * from orders where laundryId = :laundryId order by bucket limit 1", nativeQuery = true)
    Optional<OrderEntity> findNextOrderInQueue(@Param("laundryId") int laundryId);
}
