package com.microservices.taskcoordinator.repository;

import com.microservices.taskcoordinator.entity.LaundryStateEntity;
import org.springframework.data.repository.CrudRepository;

public interface LaundryStateRepository extends CrudRepository<LaundryStateEntity, Integer> {
}
