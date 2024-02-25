package com.fjut.healthydietapp;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.fjut.healthydietapp.mapper")
public class HealthyDietAppApplication {
    public static void main(String[] args) {
        SpringApplication.run(HealthyDietAppApplication.class, args);
    }
}
