package com.microservices.ordermanagement.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableFeignClients("com.microservices.tariffmanagement.api.feign")
public class OrderManagementService {

    public static void main(String[] args) {
        SpringApplication.run(OrderManagementService.class, args);
    }
}
