package com.microservices.taskcoordinator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableScheduling
@EnableRetry
@EnableSwagger2
public class TaskCoordinatorService {

    public static void main(String[] args) {
        SpringApplication.run(TaskCoordinatorService.class, args);
    }
}
