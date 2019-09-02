package com.microservices.laundrymanagement.configuration;

import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.OrderEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static org.modelmapper.config.Configuration.AccessLevel.PRIVATE;

@Configuration
public class OrderServiceBeansConfig {

    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(PRIVATE);

        Converter<OrderSubmissionDto, OrderEntity> toOrderEntityPostConverter = context -> {
            context.getDestination().setEstimatedTime(context.getSource().getDetails().stream()
                    .map(DetailSubmissionDto::getDuration)
                    .reduce(0L, Long::sum));

            return context.getDestination();
        };

        modelMapper.addMappings(new PropertyMap<OrderSubmissionDto, OrderEntity>() {
            @Override
            protected void configure() {
                map().setId(source.getOrderId());
            }
        }).setPostConverter(toOrderEntityPostConverter);

        return modelMapper;
    }
}
