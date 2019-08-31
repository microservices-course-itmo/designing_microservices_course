package com.microservices.laundrymanagement.configuration;

import com.microservices.laundrymanagement.dto.DetailSubmissionDto;
import com.microservices.laundrymanagement.dto.OrderSubmissionDto;
import com.microservices.laundrymanagement.entity.OrderEntity;
import com.microservices.laundrymanagement.entity.OrderStatus;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
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

        //This style of code needed because otherwise the converter will not work
        //toOrderEntityConverter needed because without it, a field mismatch error occurs
        Converter<OrderSubmissionDto, OrderEntity> toOrderEntityConverter = new Converter<OrderSubmissionDto, OrderEntity>() {
            @Override
            public OrderEntity convert(MappingContext<OrderSubmissionDto, OrderEntity> context) {
                OrderSubmissionDto source = context.getSource();
                OrderEntity destination = context.getDestination() == null ? new OrderEntity() : context.getDestination();
                destination.setId(source.getOrderId());
                destination.setSubmittedTime(System.currentTimeMillis());
                destination.setEstimatedTime(source.getDetails().stream()
                        .map(DetailSubmissionDto::getDuration)
                        .reduce(0L, Long::sum));
                destination.setStatus(OrderStatus.QUEUED);
                destination.setLaundryId(source.getLaundryId());
                destination.setBucket(source.getBucket());
                return destination;
            }
        };

        modelMapper.addConverter(toOrderEntityConverter);

        return modelMapper;
    }
}
