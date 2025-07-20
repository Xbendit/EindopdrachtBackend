package com.ben.Backend_eindopdracht;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(
        exclude = {
                SecurityAutoConfiguration.class
        }
)
public class mainBackendEindopdracht {
    public static void main(String[] args) {
        SpringApplication.run(BackendEindopdrachtApplication.class, args);
    }
}
