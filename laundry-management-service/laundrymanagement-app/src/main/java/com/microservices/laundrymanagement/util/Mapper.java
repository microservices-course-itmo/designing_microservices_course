package com.microservices.laundrymanagement.util;


/**
 * Basic interface for mappers
 *
 * @param <E> Entity
 * @param <D> Dto
 */
public interface Mapper<E, D> {

    /**
     * Maps dto to entity
     */
    E mapToEntity(D inputDto);

    /**
     * Maps entity to dto
     */
    D mapToDto(E inputEntity);
}
