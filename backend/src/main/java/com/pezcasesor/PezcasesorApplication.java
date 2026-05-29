package com.pezcasesor;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PezcasesorApplication {
    public static void main(String[] args) {
        SpringApplication.run(PezcasesorApplication.class, args);
    }
}
