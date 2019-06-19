package com.microservices.laundrymanagement.repository;

import com.microservices.laundrymanagement.model.Laundry;
import org.springframework.stereotype.Repository;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Repository
public class LaundryStubRepository implements LaundryRepository {
    private Map<Integer, Laundry> storage = new HashMap<>();


    @Override
    public Iterable<Laundry> findAll() {
        return storage.values();
    }

    @Override
    public Optional<Laundry> findById(Integer integer) {
        return Optional.ofNullable(storage.get(integer));
    }

    @Override
    public <S extends Laundry> S save(S entity) {
        throw new NotImplementedException();
    }
}
