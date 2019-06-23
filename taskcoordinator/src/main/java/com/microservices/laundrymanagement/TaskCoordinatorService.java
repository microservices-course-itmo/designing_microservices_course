package com.microservices.laundrymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
public class TaskCoordinatorService {

    public static void main(String[] args) {
        SpringApplication.run(TaskCoordinatorService.class, args);
    }
}
