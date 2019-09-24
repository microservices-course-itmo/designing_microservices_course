package com.microservices.tariffmanagement.repository;

import com.microservices.tariffmanagement.entity.TariffEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TariffRepository extends CrudRepository<TariffEntity, Integer> {
    Optional<TariffEntity> findByName(String tariffName);
}
