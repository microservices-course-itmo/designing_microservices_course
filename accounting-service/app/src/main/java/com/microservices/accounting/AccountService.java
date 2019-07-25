package com.microservices.accounting;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

/**
 * Enabling Swagger allows to see all {@link com.microservices.accounting.controller.AccountingServiceController} endpoints
 * and send request using UI.
 * <br/>
 * Swagger configuration in json format: http://{host}:{port}/v2/api-docs
 * <br/>
 * Swagger UI: http://{host}:{port}/swagger-ui.html
 */
@SpringBootApplication
@EnableSwagger2
public class AccountService {
    public static void main(String[] args) {
        SpringApplication.run(AccountService.class, args);
    }
}