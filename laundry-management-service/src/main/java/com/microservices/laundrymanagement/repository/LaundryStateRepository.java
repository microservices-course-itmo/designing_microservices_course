package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.LaundryStateEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryStateRepository extends CrudRepository<LaundryStateEntity, Integer> {
}


