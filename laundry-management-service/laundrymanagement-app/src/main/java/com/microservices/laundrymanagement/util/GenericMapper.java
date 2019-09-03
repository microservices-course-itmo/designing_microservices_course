package com.microservices.laundrymanagement.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

/**
 * Basic implementation of {@link Mapper} interface. All "real" mappers, that
 * are will be used in application, are have to constructed inside Spring's config class
 * by setting values to a fields of this class and specifying E and D types. Model mapper can be
 * customized by {@link Converter}s also inside Spring's configuration class.
 *
 * @param <E> Entity
 * @param <D> Dto
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GenericMapper<E, D> implements Mapper<E, D> {

    private Class<E> entityClass;
    private Class<D> dtoClass;
    private ModelMapper modelMapper;

    /**
     * @see Mapper#mapToEntity(Object)
     */
    @Override
    public E mapToEntity(D inputDto) {
        return modelMapper.map(inputDto, entityClass);
    }

    /**
     * @see Mapper#mapToDto(Object)
     */
    @Override
    public D mapToDto(E inputEntity) {
        return modelMapper.map(inputEntity, dtoClass);
    }
}
