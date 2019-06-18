package com.microservices.laundrymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Сервис управления прачечными. Контролирует выполнение заказов несколькими прачечными
 */
@SpringBootApplication
public class LaundryServiceManagement {
    public static void main(String[] args) {
        SpringApplication.run(LaundryServiceManagement.class, args);
    }
}
