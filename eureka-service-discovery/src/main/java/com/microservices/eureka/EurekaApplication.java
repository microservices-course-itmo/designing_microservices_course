package com.microservices.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Entry point for the Eureka server application.
 * <p>
 * Application provides service discovery mechanism - each service knows the addresses
 * of existing Eureka instances and sends some heartbeats to it periodically. So long
 * as Eureka receives these heartbeats from service it is considered to be registered.
 * It's easier to think about Eureka like it was just a map where the key is some
 * service name and the value is the list of working instances of this service. Each
 * service may request for the list of working instances by passing the servicename.
 */
@SpringBootApplication
@EnableEurekaServer
public class EurekaApplication {

    public static void main(String[] args) {
        SpringApplication.run(EurekaApplication.class, args);
    }

}
