package com.microservices.laundrymanagement.util;


import java.util.List;

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

    /**
     * Maps list of dtos to list of entities
     */
    List<E> mapToEntityList(List<D> inputDtoList);

    /**
     * Maps list of entites to list of dtos
     */
    List<D> mapToDtoList(List<E> inputEntityList);
}
