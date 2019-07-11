package com.microservices.taskcoordinator.repository;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface LaundryStateRepository extends CrudRepository<LaundryStateEntity, Integer> {

    @Query(value = "select ls from LaundryStateEntity ls " +
            "where (ls.queueWaitingTime + ls.reservedTime) = " +
            "   (select min(ls2.queueWaitingTime + ls2.reservedTime) " +
            "from LaundryStateEntity ls2)")
    Optional<LaundryStateEntity> getLeastLoadedLaundry();
}