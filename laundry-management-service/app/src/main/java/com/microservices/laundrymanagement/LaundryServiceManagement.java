package com.microservices.laundrymanagement;

import com.microservices.laundrymanagement.util.LaundryWorkingActivityEmulator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableSwagger2
@EnableScheduling
public class LaundryServiceManagement {

    /**
     * TODO temporary solution for resting purposes
     */
    @Bean
    CommandLineRunner start(LaundryWorkingActivityEmulator emulator) {
        return (args) -> emulator.run();
    }

    public static void main(String[] args) {
        SpringApplication.run(LaundryServiceManagement.class, args);
    }
}
