package org.api.pjaidapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
@EnableJpaRepositories(basePackages = "org.api.pjaidapp.repository")
@EntityScan(basePackages = "org.api.pjaidapp.model")
public class PjaidAppApplication {

    public static void main(String[] args) {
        SpringApplication.run(PjaidAppApplication.class, args);
    }

}
