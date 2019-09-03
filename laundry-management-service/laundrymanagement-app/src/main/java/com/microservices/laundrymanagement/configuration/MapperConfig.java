package com.microservices.laundrymanagement.configuration;

import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.util.GenericMapper;
import com.microservices.laundrymanagement.util.Mapper;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class MapperConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public Mapper<OrderEntity, OrderSubmissionDto> orderMapper(ModelMapper modelMapper) {
        Converter<List<DetailSubmissionDto>, Long> toOrderEntityPostConverter = context ->
                context.getSource().stream()
                        .map(DetailSubmissionDto::getDuration)
                        .reduce(0L, Long::sum);

        modelMapper.addMappings(new PropertyMap<OrderSubmissionDto, OrderEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getOrderId());
                using(toOrderEntityPostConverter)
                        .map(source.getDetails(), destination.getEstimatedTime());
            }
        });

        GenericMapper<OrderEntity, OrderSubmissionDto> genericMapper = new GenericMapper<>();
        genericMapper.setEntityClass(OrderEntity.class);
        genericMapper.setDtoClass(OrderSubmissionDto.class);
        genericMapper.setModelMapper(modelMapper);
        return genericMapper;
    }

}
