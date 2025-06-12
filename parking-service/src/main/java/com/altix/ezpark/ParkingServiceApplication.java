package com.altix.ezpark;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ParkingServiceApplication
{
    public static void main(String[] args) {
        SpringApplication.run(ParkingServiceApplication.class, args);
    }
}
