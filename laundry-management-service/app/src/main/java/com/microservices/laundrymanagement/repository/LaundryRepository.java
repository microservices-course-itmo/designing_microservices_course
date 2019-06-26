package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.entity.LaundryEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LaundryRepository extends CrudRepository<LaundryEntity, Integer> {
    boolean existsByName(String name);
}
