package com.microservices.laundrymanagement.repository;

import java.util.Optional;

public interface CrudRepository<T, ID> {
    Iterable<T> findAll();

    Optional<T> findById(ID id);

    <S extends T> S save(S entity);
}
