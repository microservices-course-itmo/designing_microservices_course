package com.microservices.laundrymanagement;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Сервис управления прачечными. Контролирует выполнение заказов несколькими прачечными
 */
@SpringBootApplication
public class LaundryService {
    public static void main(String[] args) {
        SpringApplication.run(LaundryService.class, args);
    }
}
