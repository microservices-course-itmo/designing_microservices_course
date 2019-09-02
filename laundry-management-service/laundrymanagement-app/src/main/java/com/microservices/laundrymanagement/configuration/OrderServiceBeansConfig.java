package com.microservices.laundrymanagement.configuration;

import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderServiceBeansConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();

        modelMapper.addMappings(new PropertyMap<OrderSubmissionDto, OrderEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getOrderId());
            }
        }).setPostConverter(toOrderEntityConverter);

        return modelMapper;
    }

    private Converter<OrderSubmissionDto, OrderEntity> toOrderEntityConverter = context -> {
        OrderSubmissionDto source = context.getSource();
        OrderEntity destination = context.getDestination();

        destination.setSubmittedTime(System.currentTimeMillis());
        destination.setEstimatedTime(source.getDetails().stream()
                .map(DetailSubmissionDto::getDuration)
                .reduce(0L, Long::sum));
        destination.setStatus(OrderStatus.QUEUED);

        return context.getDestination();
    };
}
