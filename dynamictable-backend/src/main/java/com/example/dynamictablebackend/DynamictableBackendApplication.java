package com.example.dynamictablebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;


@SpringBootApplication
@ComponentScan(basePackages = "com.example.dynamictablebackend")
public class DynamictableBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(DynamictableBackendApplication.class, args);
    }

}
