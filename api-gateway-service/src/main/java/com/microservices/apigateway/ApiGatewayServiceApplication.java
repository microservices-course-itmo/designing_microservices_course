package com.microservices.apigateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import springfox.documentation.swagger2.annotations.EnableSwagger2;


/**
 * Entry point for API Gateway application
 * <p>
 * API Gateway is the only service which exposes it's REST API to UI clients.
 * Other services can not be directly accessed from the outside but they still
 * may consume each other REST API directly. Generally the API Gateway acts like
 * a reverse proxy - it is pretending to be the the server which handles clients
 * request but in fact their request is processed by some certain back end service.
 */
@SpringBootApplication
@EnableZuulProxy
@EnableSwagger2
public class ApiGatewayServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayServiceApplication.class, args);
    }

}
