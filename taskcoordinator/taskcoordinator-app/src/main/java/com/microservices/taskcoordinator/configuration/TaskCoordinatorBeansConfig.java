package com.microservices.taskcoordinator.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TaskCoordinatorBeansConfig {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
}
