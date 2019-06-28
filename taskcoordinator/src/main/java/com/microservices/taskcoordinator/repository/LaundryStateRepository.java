package com.microservices.taskcoordinator.repository;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

public interface LaundryStateRepository extends CrudRepository<LaundryStateEntity, Integer> {

    @Query(value = "select ls from LaundryStateEntity l " +
            "where (l.queueWaitingTime + l.reservedTime) = " +
            "   (select max(ls2.queueWaitingTime + ls2.reservedTime) " +
            "from LaundryStateEntity ls2)")
    LaundryStateEntity getLeastLoadedLaundry();
}