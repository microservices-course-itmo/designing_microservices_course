package com.microservices.tariffmanagement.repository;

import com.microservices.tariffmanagement.entity.TariffEntity;
import org.springframework.data.repository.CrudRepository;

public interface TariffRepository extends CrudRepository<TariffEntity, Long> {
}
