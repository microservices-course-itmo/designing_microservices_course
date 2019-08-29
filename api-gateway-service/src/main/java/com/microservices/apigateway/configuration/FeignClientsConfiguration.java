package com.microservices.apigateway.configuration;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
/*@EnableFeignClients({"com.microservices.tariffmanagement.api", "com.microservices.ordermanagement.api"})
@ComponentScan({"com.microservices.tariffmanagement.api", "com.microservices.ordermanagement.api"})*/
@EnableFeignClients("com.microservices.**.api")
@ComponentScan("com.microservices.**.api")
public class FeignClientsConfiguration {
}
