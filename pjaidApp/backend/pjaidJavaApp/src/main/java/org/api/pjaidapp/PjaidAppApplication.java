package org.api.pjaidapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class PjaidAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PjaidAppApplication.class, args);
    }

}
