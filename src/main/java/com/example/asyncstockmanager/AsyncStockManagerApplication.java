package com.example.asyncstockmanager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class AsyncStockManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(AsyncStockManagerApplication.class, args);
    }

}
