package com.microservices.configserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

/**
 * Entry point for the Configuration service application.
 *
 * Application is responsible for providing services with configuration properties.
 * Properties are stored in the resources/config folder. Configuration server supports configuring
 * applications according to environment (actually spring profiles) where the application
 * is running, application name, common properties
 */
@EnableConfigServer
@SpringBootApplication
public class ConfigurationServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ConfigurationServiceApplication.class, args);
    }

}
