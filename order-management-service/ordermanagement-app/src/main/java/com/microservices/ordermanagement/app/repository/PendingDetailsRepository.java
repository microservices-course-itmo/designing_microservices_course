package com.microservices.ordermanagement.app.repository;

import com.microservices.ordermanagement.app.entity.PendingDetailEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PendingDetailsRepository extends CrudRepository<PendingDetailEntity, Integer> {
}
